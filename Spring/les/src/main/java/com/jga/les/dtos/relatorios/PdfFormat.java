package com.jga.les.dtos.relatorios;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PdfFormat {
    String datePattern() default "dd/MM/yyyy";
    String dateTimePattern() default "dd/MM/yyyy HH:mm";
    String numberPattern() default "#,##0.00";
    String nullValue() default "N/A";
}
