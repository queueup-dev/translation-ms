package com.sfl.tms.rest.common.communicator.translation.response.aggregation.multiple

import com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple.TranslationAggregationByLanguage
import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel

/**
 * User: Vazgen Danielyan
 * Date: 1/18/19
 * Time: 12:39 PM
 */
data class TranslationAggregationByEntityResponseModel(var uuid: String, var label: String, var languages: List<TranslationAggregationByLanguage>) : AbstractApiResponseModel