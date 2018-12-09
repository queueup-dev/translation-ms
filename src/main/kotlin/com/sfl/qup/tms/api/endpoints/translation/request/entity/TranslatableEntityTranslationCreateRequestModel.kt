package com.sfl.qup.tms.api.endpoints.translation.request.entity

import com.sfl.qup.tms.api.common.model.error.ErrorModel
import com.sfl.qup.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.qup.tms.api.endpoints.translation.error.TranslationControllerErrorType
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 11:59 PM
 */
data class TranslatableEntityTranslationCreateRequestModel(val text: String, val lang: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorModel> = ArrayList<ErrorModel>()
            .apply {
                if (StringUtils.isEmpty(text)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_ENTITY_TRANSLATION_TEXT_MISSING))
                }
            }.apply {
                if (StringUtils.isEmpty(lang)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_ENTITY_TRANSLATION_LANG_MISSING))
                }
            }

}