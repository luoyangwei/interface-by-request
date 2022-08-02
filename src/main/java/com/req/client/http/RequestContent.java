package com.req.client.http;

import com.alibaba.fastjson.JSON;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author luoyangwei by 2022-08-02 20:25 created
 */
@ToString
public class RequestContent {

    private final RequestMediaType mediaType;
    private String string;
    private Map<String, Object> formData;

    public RequestContent(RequestMediaType mediaType) {
        this.mediaType = mediaType;
    }

    public RequestContent(RequestMediaType mediaType, String string) {
        this.mediaType = mediaType;
        this.string = string;
    }

    public RequestMediaType getMediaType() {
        return mediaType;
    }

    public String getString() {
        return string;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public void setRequestBody(Object body) {
        if (RequestMediaType.APPLICATION_JSON.equals(mediaType)) {
            this.string = JSON.toJSONString(body);
        }
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

}
