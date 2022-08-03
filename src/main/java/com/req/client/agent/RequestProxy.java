package com.req.client.agent;

import com.req.client.RequestTarget;
import com.req.client.RequestTargetFactory;
import com.req.client.http.RequestHttpImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author luoyangwei by 2022-08-02 14:37 created
 */
@Slf4j
public class RequestProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = -7735588959788873305L;
    private final RequestTargetFactory requestTargetFactory;

    public RequestProxy(RequestHttpImpl requestHttpClient) {
        requestTargetFactory = new RequestTargetFactory(requestHttpClient);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestTarget requestTarget = requestTargetFactory.newRequestTarget(method, args);
        Object result = requestTarget.send();
        log.info("result: {}", result);
        return result;
    }


}
