package com.sfl.tms.rest.common.communicator.translation.request.aggregation

import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.model.error.ErrorType
import com.sfl.tms.rest.common.model.request.AbstractApiRequestModel
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 12:40 PM
 */
data class TranslationKeyValuePair(var key: String, var value: String) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
            .apply {
                if (StringUtils.isEmpty(key)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_KEY_MISSING)
                }
            }.apply {
                if (StringUtils.isEmpty(value)) {
                    add(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_VALUE_MISSING)
                }
            }

}