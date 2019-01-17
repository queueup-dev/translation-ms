package com.sfl.tms.api.endpoints.translation.request.translation

import com.sfl.tms.api.common.model.error.ErrorType
import com.sfl.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.tms.api.endpoints.translation.error.TranslationControllerErrorType
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:22 PM
 */
data class TranslatableEntityFieldTranslationUpdateRequestModel(val value: String, val lang: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
            .apply {
                if (StringUtils.isEmpty(value)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_VALUE_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(lang)) {
                    add(TranslationControllerErrorType.LANGUAGE_LANG_MISSING)
                }
            }
}