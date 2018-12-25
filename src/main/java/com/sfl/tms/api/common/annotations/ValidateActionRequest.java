package com.sfl.tms.api.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Vazgen Danielyan
 * Date: 12/25/18
 * Time: 1:00 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface ValidateActionRequest {
}
