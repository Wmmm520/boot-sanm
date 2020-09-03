package com.sanm.annotation.jobOperation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE_USE})
@Indexed
@Component
public @interface JobOperation {


    int jobCode() default -1;

    int triggerCode() default -1;


}
