package com.req.client.http;

import com.req.client.RequestTarget;
import com.req.client.http.RequestContent;
import com.req.client.http.RequestMediaType;
import com.req.client.http.RequestMode;
import okhttp3.*;
import okhttp3.Headers;
import okhttp3.Request;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author luoyangwei by 2022-08-01 14:03 created
 */
public class RequestHttpImpl {

    private OkHttpClient httpClient;

    public RequestHttpImpl() {
    }

    public RequestHttpImpl(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Create http request
     *
     * @param url         url
     * @param headersMap  headers
     * @param requestMode request mode
     * @param content     request content
     * @return request
     */
    public okhttp3.Request createHttp(String url, Map<String, Object> headersMap, RequestMode requestMode, RequestContent content) {

        okhttp3.Headers.Builder headers = new Headers.Builder();
        if (!CollectionUtils.isEmpty(headersMap)) {
            for (Map.Entry<String, Object> entry : headersMap.entrySet()) {
                headers.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        okhttp3.Request request = null;
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder().url(url).headers(headers.build());
        if (RequestMode.POST.equals(requestMode)) {

            RequestBody requestBody = null;
            if (RequestMediaType.APPLICATION_JSON.equals(content.getMediaType())) {
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content.getString());
            } else if (RequestMediaType.FORM_DATA.equals(content.getMediaType())) {
                FormBody.Builder builder = new FormBody.Builder();
                Map<String, Object> formDataMap = content.getFormData();
                for (Map.Entry<String, Object> formData : formDataMap.entrySet()) {
                    builder.add(formData.getKey(), String.valueOf(formData.getValue()));
                }
                requestBody = builder.build();
            }

            request = requestBuilder.post(Objects.requireNonNull(requestBody, "Incorrect media type!"))
                    .build();

        } else if (RequestMode.GET.equals(requestMode)) {
            request = requestBuilder.get().build();
        }
        return request;
    }

    public Response newCall(Request request) {
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object send(RequestTarget requestTarget) {
        try {
            return requestTarget.send();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
