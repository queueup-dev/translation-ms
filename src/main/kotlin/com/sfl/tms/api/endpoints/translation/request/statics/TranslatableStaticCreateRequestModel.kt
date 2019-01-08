package com.sfl.tms.api.endpoints.translation.request.statics

import com.sfl.tms.api.common.model.error.ErrorType
import com.sfl.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.tms.api.endpoints.translation.error.TranslationControllerErrorType
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:22 PM
 */
data class TranslatableStaticCreateRequestModel(val key: String, val entityUuid: String, val value: String, val lang: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
            .apply {
                if (StringUtils.isEmpty(key)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_STATIC_KEY_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(entityUuid)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_STATIC_ENTITY_UUID_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(value)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_STATIC_VALUE_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(lang)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_STATIC_LANGUAGE_MISSING)
                }
            }
}