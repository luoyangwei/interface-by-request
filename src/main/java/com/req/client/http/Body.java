package com.req.client.http;

import java.lang.annotation.*;

/**
 * @author luoyangwei by 2022-08-02 15:25 created
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Body {

    String value() default "";

}
