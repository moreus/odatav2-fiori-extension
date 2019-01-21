package com.sap.odatav2.annotation.extensions.annotation;

import com.sap.odatav2.annotation.extensions.annotation.property.Label;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ModelEntityType {
    int contentVersion() default 0;
    Label label() default @Label(text = "");
}
