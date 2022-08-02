package com.req.client.http;

/**
 * @author luoyangwei by 2022-08-01 14:20 created
 */
public enum RequestMediaType {

    /**
     * json
     */
    APPLICATION_JSON,

    /**
     * 表单
     */
    FORM_DATA,

    /**
     * 链接拼接
     */
    X_WWW_FORM_URLENCODED;

    public static RequestMediaType getInstance(String mediaType) {
        if ("application/json".contains(mediaType)) {
            return APPLICATION_JSON;
        }
        if ("form-data".contains(mediaType)) {
            return FORM_DATA;
        }
        if ("x_www_form_urlencoded".contains(mediaType)) {
            return X_WWW_FORM_URLENCODED;
        }
        return null;
    }

}
