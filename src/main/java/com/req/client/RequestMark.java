package com.req.client;

import java.lang.annotation.*;

/**
 * @author luoyangwei by 2022-08-01 14:53 created
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RequestMark {

    String[] value() default {};

    String hostname();

}
