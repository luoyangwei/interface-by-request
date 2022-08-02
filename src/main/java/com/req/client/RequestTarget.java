package com.req.client;

import com.alibaba.fastjson.JSON;
import com.req.client.http.RequestHttpImpl;
import com.req.client.http.Header;
import com.req.client.http.RequestContent;
import com.req.client.http.RequestMediaType;
import com.req.client.http.RequestMode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author luoyangwei by 2022-08-02 15:50 created
 */
@Slf4j
public class RequestTarget {

    /**
     * 请求工具具体实现
     */
    private RequestHttpImpl requestHttp;

    private String hostname;
    private String url;
    private RequestMode requestMode;
    private List<Header> headers;
    private RequestMediaType requestMediaType;

    private Class<?> resultType;
    private Class<?> bodyType;
    private Object requestBody;

    public RequestTarget() {
    }

    public RequestTarget(String hostname, RequestHttpImpl requestHttp) {
        this.hostname = hostname;
        this.requestHttp = requestHttp;
    }

    /**
     * 发送请求
     *
     * @return response
     */
    public Object send() throws IOException {

        Map<String, Object> headersMap = new HashMap<>();
        if (null != headers) {
            for (Header header : headers) {
                headersMap.put(header.key(), header.val());
            }
        }
        log.info("Request readying headers: {}", headersMap);

        RequestContent requestContent = new RequestContent(requestMediaType);
        requestContent.setRequestBody(requestBody);
        log.info("Request readying body: {}", requestContent);

        RequestHttpImpl http = Objects.requireNonNull(requestHttp, "the Request http impl is empty!");
        Request request = http.createHttp(completeHostname(), headersMap, requestMode, requestContent);
        Response response = http.newCall(request);
        if (null != response && response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (null != responseBody) {
                String responseText = responseBody.string();
                log.info("Request http successful! received response: {}", responseText);
                return JSON.parseObject(responseText, resultType);
            }
        }

        return null;
    }

    public String completeHostname() {
        if (null == hostname || hostname.isEmpty()) {
            throw new IllegalArgumentException("the hostname is empty");
        }
        if (null == url || url.isEmpty()) {
            throw new IllegalArgumentException("the url is empty");
        }
        return hostname + url;
    }


    public RequestHttpImpl getRequestHttp() {
        return requestHttp;
    }

    public void setRequestHttp(RequestHttpImpl requestHttp) {
        this.requestHttp = requestHttp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUrl() {
        return url;
    }

    public RequestTarget setUrl(String url) {
        this.url = url;
        return this;
    }

    public RequestTarget setRequestMode(RequestMode requestMode) {
        this.requestMode = requestMode;
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public RequestTarget setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public RequestMediaType getRequestContentType() {
        return requestMediaType;
    }

    public RequestTarget setRequestMediaType(RequestMediaType requestMediaType) {
        this.requestMediaType = requestMediaType;
        return this;
    }

    public RequestTarget setResultType(Class<?> resultType) {
        this.resultType = resultType;
        return this;
    }

    public RequestTarget setBodyType(Class<?> bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public RequestTarget setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
        this.bodyType = requestBody.getClass();
        return this;
    }
}
