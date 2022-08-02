package com.req.client.http;

/**
 * @author luoyangwei by 2022-08-02 15:24 created
 */
public @interface Header {

    String key() default "";

    String val() default "";

}
