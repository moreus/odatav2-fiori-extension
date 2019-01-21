package com.sap.odatav2.annotation.extensions.annotation;

import com.sap.odatav2.annotation.extensions.annotation.property.Label;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ModelEntitySet {
    boolean addressable() default true;
    boolean creatable() default true;
    boolean deletable() default true;
    boolean updatable() default true;
    boolean searchable() default false;
    boolean pageable() default true;
    boolean subscribable() default true;
    boolean requireFilter() default false;
    boolean topable() default true;
    boolean countable() default true;
    boolean changeTracking() default false;
    String deletablePath() default "";
    String updatablePath() default "";
    int contentVersion() default 0;
    Label label() default @Label(text = "");
}