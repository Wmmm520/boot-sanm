package com.sanm.annotation.jobOperationScan;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Author: Sanm
 * since: v1.0
 * description: 在启动类上加入这个注解,即可扫描
 **/
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import({JobOperationScanRegister.class})
public @interface JobOperationScan {

    String[] basePackages() default {};
}
