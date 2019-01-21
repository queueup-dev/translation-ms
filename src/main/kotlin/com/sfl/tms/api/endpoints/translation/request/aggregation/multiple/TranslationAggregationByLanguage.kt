package com.sfl.tms.api.endpoints.translation.request.aggregation.multiple

import com.sfl.tms.api.common.model.error.ErrorType
import com.sfl.tms.api.common.model.request.AbstractApiRequestModel
import com.sfl.tms.api.endpoints.translation.error.TranslationControllerErrorType
import com.sfl.tms.api.endpoints.translation.request.aggregation.TranslationKeyValuePair
import org.apache.commons.lang3.StringUtils

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 6:45 PM
 */
data class TranslationAggregationByLanguage(val lang: String, val keys: List<TranslationKeyValuePair>) : AbstractApiRequestModel() {
    override fun validateRequiredFields(): List<ErrorType> = ArrayList<ErrorType>()
            .apply {
                if (StringUtils.isEmpty(lang)) {
                    add(TranslationControllerErrorType.LANGUAGE_LANG_MISSING)
                }
            }.apply {
                keys.map { it.validateRequiredFields() }.flatten().distinct().let { addAll(it) }
            }

}
