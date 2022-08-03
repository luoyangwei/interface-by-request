package com.req.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.ResolvableType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author luoyangwei by 2022-08-03 17:03 created
 */
public class ObjectMapperResponseHandler extends AbstractResponseHandler {
    private static ObjectMapper objectMapper;

    private Method method;
    private Object[] args;
    private ParameterizedType[] types;

    public ObjectMapperResponseHandler() {
    }

    public ObjectMapperResponseHandler(Method method, Object[] args, ParameterizedType[] types) {
        this.method = method;
        this.args = args;
        this.types = types;
    }

    public synchronized ObjectMapper getDefaultObjectMapper() {
        if (null == objectMapper) {
            ObjectMapper defaultObjectMapper = new ObjectMapper();

            //configure方法 配置一些需要的参数
            // 转换为格式化的json 显示出来的格式美化
            defaultObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            //序列化的时候序列对象的那些属性
            //JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
            //JsonInclude.Include.ALWAYS      所有属性
            //JsonInclude.Include.NON_EMPTY   属性为 空（“”） 或者为 NULL 都不序列化
            //JsonInclude.Include.NON_NULL    属性为NULL 不序列化
            defaultObjectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

            //反序列化时,遇到未知属性会不会报错
            //true - 遇到没有的属性就报错 false - 没有的属性不会管，不会报错
            defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            //如果是空对象的时候,不抛异常
            defaultObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            //修改序列化后日期格式
            defaultObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            defaultObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            //处理不同的时区偏移格式
            defaultObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            defaultObjectMapper.registerModule(new JavaTimeModule());

            objectMapper = defaultObjectMapper;
            return defaultObjectMapper;
        } else {
            return objectMapper;
        }
    }

    public Object readValue(String responseText, Class<?> returnType) {
        Object jsonObject = JSON.parseObject(responseText, returnType);
        Field[] jsonFields = jsonObject.getClass().getDeclaredFields();
        for (Field jsonField : jsonFields) {
            jsonField.setAccessible(true);
            Type genericType = jsonField.getGenericType();
            if (genericType instanceof ParameterizedType) {
                try {
                    Object object = jsonField.get(jsonObject);
                    if (object instanceof List) {
                        List<?> list = ((List<?>) object);
                        List<?> newList = new ArrayList<>();
                        for (Object item : list) {
                            ParameterizedType parameterizedType = types[0];
                            newList.add(((JSONObject) item).toJavaObject(parameterizedType.getActualTypeArguments()[0]));
                        }
                        jsonField.set(jsonObject, newList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

}
