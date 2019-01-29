package com.sfl.tms.rest.common.communicator.translation.error

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.sfl.tms.rest.common.model.error.ErrorType

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:26 PM
 */
@JsonTypeName("translationControllerErrorType")
enum class TranslationControllerErrorType : ErrorType {

    //region language

    //region validation

    LANGUAGE_LANG_MISSING,

    //endregion

    //region exception

    LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION,
    LANGUAGE_EXIST_BY_LANG_EXCEPTION,

    //endregion

    //endregion

    //region entity

    //region validation

    TRANSLATABLE_ENTITY_UUID_MISSING,
    TRANSLATABLE_ENTITY_LABEL_MISSING,
    TRANSLATABLE_ENTITY_NAME_MISSING,

    //endregion

    //region exception

    TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION,
    TRANSLATABLE_ENTITY_EXISTS_BY_UUID_EXCEPTION,

    //endregion

    //endregion

    //region field

    //region validation

    TRANSLATABLE_ENTITY_FIELD_KEY_MISSING,

    //endregion

    //region exception

    TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION,
    TRANSLATABLE_ENTITY_FIELD_NOT_FOUND_EXCEPTION,

    //endregion

    //endregion

    //region translation

    //region validation

    TRANSLATABLE_ENTITY_FIELD_TRANSLATION_VALUE_MISSING,

    //endregion

    //region exception

    TRANSLATABLE_ENTITY_FIELD_TRANSLATION_EXIST_EXCEPTION,
    TRANSLATABLE_ENTITY_FIELD_TRANSLATION_NOT_FOUND_EXCEPTION,

    //endregion

    //endregion
}