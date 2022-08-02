package com.req.client.http;

import java.lang.annotation.*;

/**
 * @author luoyangwei by 2022-08-02 15:26 created
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Patch {

    String[] value() default {};

    String url() default "";

}
