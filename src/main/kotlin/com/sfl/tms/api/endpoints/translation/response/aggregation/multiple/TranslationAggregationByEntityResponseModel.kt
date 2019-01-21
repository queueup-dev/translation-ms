package com.sfl.tms.api.endpoints.translation.response.aggregation.multiple

import com.sfl.tms.api.common.model.response.AbstractApiResponseModel
import com.sfl.tms.api.endpoints.translation.request.aggregation.multiple.TranslationAggregationByLanguage

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 12:39 PM
 */
data class TranslationAggregationByEntityResponseModel(val uuid: String, val label: String, val languages: List<TranslationAggregationByLanguage>) : AbstractApiResponseModel