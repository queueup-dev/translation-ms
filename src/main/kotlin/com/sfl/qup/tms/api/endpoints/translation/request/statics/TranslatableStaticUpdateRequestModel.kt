package com.sfl.qup.tms.api.endpoints.translation.request.statics

import com.sfl.qup.tms.api.common.model.error.ErrorModel
import com.sfl.qup.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.qup.tms.api.endpoints.translation.error.TranslationControllerErrorType
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 4:22 PM
 */
data class TranslatableStaticUpdateRequestModel(val key: String, val value: String, val languageId: Long) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorModel> = ArrayList<ErrorModel>()
            .apply {
                if (StringUtils.isEmpty(key)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_STATIC_KEY_MISSING))
                }
            }.apply {
                if (StringUtils.isEmpty(value)) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_STATIC_VALUE_MISSING))
                }
            }.apply {
                if (languageId <= 0) {
                    add(ErrorModel(TranslationControllerErrorType.TRANSLATABLE_STATIC_LANGUAGE_ID_INCORRECT))
                }
            }
}