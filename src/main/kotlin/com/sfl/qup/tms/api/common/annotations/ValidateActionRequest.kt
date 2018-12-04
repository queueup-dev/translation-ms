package com.sfl.qup.tms.api.common.annotations

/**
 * User: Vazgen Danielyan
 * Company: SFL LLC
 * Date: 1/25/16
 * Time: 11:20 PM
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD)
annotation class ValidateActionRequest
