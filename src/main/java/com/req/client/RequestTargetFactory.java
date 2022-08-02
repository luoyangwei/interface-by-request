package com.req.client;

import com.req.client.http.RequestHttpImpl;
import com.req.client.http.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author luoyangwei by 2022-08-02 15:51 created
 */
@Slf4j
public class RequestTargetFactory {

    private final RequestHttpImpl requestHttpClient;

    public RequestTargetFactory(RequestHttpImpl requestHttpClient) {
        this.requestHttpClient = requestHttpClient;
    }

    public RequestTarget newRequestTarget(Method method, Object[] args) {
        return processRequestTarget(method, args);
    }

    private RequestTarget processRequestTarget(Method method, Object[] args) {
        RequestMark requestMark = getMarkBeanAnnotation(method.getDeclaringClass());
        RequestAnnotationNode request = getRequestAnnotationNode(method);
        RequestHeaderAnnotationNode headers = getHeadersAnnotationNode(method);
        Object requestBody = getBody(method.getParameters(), args);

        RequestTarget requestTarget = new RequestTarget(requestMark.hostname(), requestHttpClient);
        requestTarget.setUrl(request.getUrl())
                .setRequestMode(request.getRequestMode())
                .setResultType(method.getReturnType());
        if (null != requestBody) {
            requestTarget.setRequestBody(requestBody);
        }
        if (null != headers) {
            requestTarget.setHeaders(Arrays.asList(headers.getOriginHeaders().headers()))
                    .setRequestMediaType(headers.getMediaType());
        }

        return requestTarget;
    }

    /**
     * Mark bean annotation
     *
     * @param declaringClass bean class
     * @return {@link  RequestMark}
     */
    public RequestMark getMarkBeanAnnotation(Class<?> declaringClass) {
        return Objects.requireNonNull(declaringClass.getAnnotation(RequestMark.class), "Class is not marked！");
    }

    public RequestHeaderAnnotationNode getHeadersAnnotationNode(Method method) {
        Headers headers = method.getAnnotation(Headers.class);
        return new RequestHeaderAnnotationNode(headers);
    }

    public RequestAnnotationNode getRequestAnnotationNode(Method method) {
        Request request = method.getAnnotation(Request.class);
        if (null == request) {
            Post post = method.getAnnotation(Post.class);
            Get get = method.getAnnotation(Get.class);
            Put put = method.getAnnotation(Put.class);
            Patch patch = method.getAnnotation(Patch.class);
            Delete delete = method.getAnnotation(Delete.class);
            if (null != post) {
                return new RequestAnnotationNode(post.url(), RequestMode.POST);
            } else if (null != get) {
                return new RequestAnnotationNode(get.url(), RequestMode.GET);
            } else if (null != put) {
                return new RequestAnnotationNode(put.url(), RequestMode.PUT);
            } else if (null != patch) {
                return new RequestAnnotationNode(patch.url(), RequestMode.PATCH);
            } else if (null != delete) {
                return new RequestAnnotationNode(delete.url(), RequestMode.DELETE);
            } else {
                throw new NullPointerException("Method is not marked！");
            }

        } else {
            return new RequestAnnotationNode(request.url(), request.requestMode());
        }
    }

    public Object getBody(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            Body body = parameter.getAnnotation(Body.class);
            if (null != body) {
                return arg;
            }
        }
        return null;
    }

    private static class RequestAnnotationNode {
        private final String url;
        private final RequestMode requestMode;

        public RequestAnnotationNode(String url, RequestMode requestMode) {
            this.url = url;
            this.requestMode = requestMode;
        }

        public String getUrl() {
            return url;
        }

        public RequestMode getRequestMode() {
            return requestMode;
        }
    }

    private static class RequestHeaderAnnotationNode {
        private final Headers originHeaders;
        private static final String MEDIA_TYPE_HEADER_KEY = "Content-Type";
        private RequestMediaType mediaType;
        private final Map<String, String> headersMap = new HashMap<>();

        public RequestHeaderAnnotationNode(Headers originHeaders) {
            this.originHeaders = originHeaders;
        }

        public RequestMediaType getMediaType() {
            if (null == mediaType) {
                Header[] headers = originHeaders.headers();
                for (Header header : headers) {
                    if (MEDIA_TYPE_HEADER_KEY.equals(header.key())) {
                        this.mediaType = RequestMediaType.getInstance(header.val());
                    }
                }
            }
            return Objects.requireNonNull(mediaType, "Media type not found!");
        }

        public Headers getOriginHeaders() {
            return originHeaders;
        }
    }

}
