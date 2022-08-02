package com.req.client.http;

import java.lang.annotation.*;

/**
 * @author luoyangwei by 2022-08-02 15:36 created
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Request {

    String[] value() default {};

    String url() default "";

    RequestMode requestMode() default RequestMode.NONE;

}
