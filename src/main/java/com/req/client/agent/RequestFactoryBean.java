package com.req.client.agent;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author luoyangwei by 2022-08-01 17:57 created
 */
public class RequestFactoryBean<T> implements FactoryBean<T> {

    private Class<T> requestInterface;
    private RequestHttpImpl requestHttpClient;

    public RequestFactoryBean() {
    }

    public RequestFactoryBean(Class<T> requestInterface) {
        this.requestInterface = requestInterface;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        final RequestProxy<T> requestProxy = new RequestProxy<>(requestHttpClient);
        return (T) Proxy.newProxyInstance(requestInterface.getClassLoader(), new Class[]{requestInterface}, requestProxy);
    }

    @Override
    public Class<T> getObjectType() {
        return requestInterface;
    }

    public void setRequestHttpClient(RequestHttpImpl requestHttpClient) {
        this.requestHttpClient = requestHttpClient;
    }

}
