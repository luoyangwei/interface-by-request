package com.req.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author luoyangwei by 2022-08-01 14:31 created
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RequestClassScannerRegistrar.class)
public @interface RequestScan {

    String[] value() default {};

    String[] basePackages() default {};

}
