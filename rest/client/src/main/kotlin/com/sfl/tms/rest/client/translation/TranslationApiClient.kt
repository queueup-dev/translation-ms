package com.sfl.tms.rest.client.translation

import com.sfl.tms.rest.client.rs.model.GenericArrayResponse
import com.sfl.tms.rest.common.communicator.translation.model.TranslatableEntityFieldTypeModel
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.TranslationKeyValuePair
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple.TranslationAggregationByEntityRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.entity.TranslatableEntityCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.field.TranslatableEntityFieldCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.translation.TranslatableEntityFieldTranslationCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.translation.TranslatableEntityFieldTranslationUpdateRequestModel
import com.sfl.tms.rest.common.communicator.translation.response.aggregation.multiple.TranslationAggregationByEntityResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.aggregation.single.TranslationAggregationByKey
import com.sfl.tms.rest.common.communicator.translation.response.entity.TranslatableEntityCreateResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.field.TranslatableEntityFieldCreateResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.translation.TranslatableEntityFieldTranslationResponseModel
import com.sfl.tms.rest.common.model.ResultModel

/**
 * User: Vazgen Danielyan
 * Date: 1/27/19
 * Time: 11:49 PM
 */
interface TranslationApiClient {

    fun createTranslatableEntity(request: TranslatableEntityCreateRequestModel): ResultModel<TranslatableEntityCreateResponseModel>

    fun createTranslatableEntityField(request: TranslatableEntityFieldCreateRequestModel): ResultModel<TranslatableEntityFieldCreateResponseModel>

    fun createTranslatableEntityFieldTranslation(request: TranslatableEntityFieldTranslationCreateRequestModel): ResultModel<TranslatableEntityFieldTranslationResponseModel>

    fun updateTranslatableEntityFieldTranslation(
            uuid: String,
            label: String,
            key: String,
            type: TranslatableEntityFieldTypeModel,
            request: List<TranslatableEntityFieldTranslationUpdateRequestModel>
    ): ResultModel<GenericArrayResponse<TranslatableEntityFieldTranslationResponseModel>>

    fun getEntityFieldsWithTranslations(uuid: String, label: String, type: TranslatableEntityFieldTypeModel): ResultModel<GenericArrayResponse<TranslationAggregationByKey>>

    fun getEntityFieldsWithTranslationsWithLanguage(uuid: String, label: String, type: TranslatableEntityFieldTypeModel, lang: String): ResultModel<GenericArrayResponse<TranslationKeyValuePair>>

    fun getEntityFieldsWithNameWithTranslationsWithLanguage(uuid: String, name: String, type: TranslatableEntityFieldTypeModel, lang: String): ResultModel<GenericArrayResponse<TranslationKeyValuePair>>

    fun getEntityFieldsWithTranslationsForLanguages(uuid: String, label: String, type: TranslatableEntityFieldTypeModel, lang: List<String>): ResultModel<GenericArrayResponse<TranslationKeyValuePair>>

    fun getEntityFieldsWithNameWithTranslations(uuid: String,
                                                name: String,
                                                type: TranslatableEntityFieldTypeModel
    ): ResultModel<GenericArrayResponse<TranslationKeyValuePair>>

    fun getEntityFieldsWithNameWithTranslations(uuid: String,
                                                labels: List<String>,
                                                name: String,
                                                type: TranslatableEntityFieldTypeModel,
                                                lang: String
    ): ResultModel<GenericArrayResponse<TranslationKeyValuePair>>

    fun createOrUpdateTranslatableEntityWithDependencies(type: TranslatableEntityFieldTypeModel, request: TranslationAggregationByEntityRequestModel): ResultModel<TranslationAggregationByEntityResponseModel>
}