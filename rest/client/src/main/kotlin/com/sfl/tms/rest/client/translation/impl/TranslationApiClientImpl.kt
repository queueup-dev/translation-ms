package com.sfl.tms.rest.client.translation.impl

import com.sfl.tms.rest.client.rs.WebTargetClientService
import com.sfl.tms.rest.client.rs.model.GenericArrayResponse
import com.sfl.tms.rest.client.translation.TranslationApiClient
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
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType

/**
 * User: Vazgen Danielyan
 * Date: 2/12/19
 * Time: 12:37 AM
 */
@Service
class TranslationApiClientImpl : TranslationApiClient {

    //region Injection

    @Autowired
    private lateinit var target: WebTargetClientService

    //endregion

    //region Create translatable entity

    override fun createTranslatableEntity(request: TranslatableEntityCreateRequestModel): ResultModel<TranslatableEntityCreateResponseModel> =
        target.translationWebTarget
            .path(ENTITY_CREATE_API)
            .also { logger.debug("Calling web target '{}'", it.uri) }
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity<TranslatableEntityCreateRequestModel>(request, MediaType.APPLICATION_JSON), object : GenericType<ResultModel<TranslatableEntityCreateResponseModel>>() {})

    //endregion

    //region Create translatable entity field

    override fun createTranslatableEntityField(request: TranslatableEntityFieldCreateRequestModel): ResultModel<TranslatableEntityFieldCreateResponseModel> =
        target.translationWebTarget
            .path(ENTITY_FIELD_CREATE_API)
            .also { logger.debug("Calling web target '{}'", it.uri) }
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity<TranslatableEntityFieldCreateRequestModel>(request, MediaType.APPLICATION_JSON), object : GenericType<ResultModel<TranslatableEntityFieldCreateResponseModel>>() {})

    //endregion

    //region Create translatable entity field translation

    override fun createTranslatableEntityFieldTranslation(request: TranslatableEntityFieldTranslationCreateRequestModel): ResultModel<TranslatableEntityFieldTranslationResponseModel> =
        target.translationWebTarget
            .path(ENTITY_FIELD_TRANSLATION_CREATE_API)
            .also { logger.debug("Calling web target '{}'", it.uri) }
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity<TranslatableEntityFieldTranslationCreateRequestModel>(request, MediaType.APPLICATION_JSON), object : GenericType<ResultModel<TranslatableEntityFieldTranslationResponseModel>>() {})

    //endregion

    //region Update translatable entity field translation

    override fun updateTranslatableEntityFieldTranslation(
        uuid: String,
        label: String,
        key: String,
        type: TranslatableEntityFieldTypeModel,
        request: List<TranslatableEntityFieldTranslationUpdateRequestModel>
    ): ResultModel<GenericArrayResponse<TranslatableEntityFieldTranslationResponseModel>> = target.translationWebTarget
        .path(String.format(UPDATE_ENTITY_FIELD_TRANSLATION_CREATE_API, uuid, label, key, type.name))
        .also { logger.debug("Calling web target '{}'", it.uri) }
        .request(MediaType.APPLICATION_JSON)
        .put(
            Entity.entity<List<TranslatableEntityFieldTranslationUpdateRequestModel>>(request, MediaType.APPLICATION_JSON),
            object : GenericType<ResultModel<GenericArrayResponse<TranslatableEntityFieldTranslationResponseModel>>>() {}
        )

    //endregion

    //region Get entity fields with translations, with/without language

    override fun getEntityFieldsWithTranslations(uuid: String, label: String, type: TranslatableEntityFieldTypeModel): ResultModel<GenericArrayResponse<TranslationAggregationByKey>> =
        target.translationWebTarget
            .path(String.format(GET_ENTITY_FIELD_TRANSLATION_API, uuid, label))
            .also { logger.debug("Calling web target '{}'", it.uri) }
            .request(MediaType.APPLICATION_JSON)
            .get(object : GenericType<ResultModel<GenericArrayResponse<TranslationAggregationByKey>>>() {})

    override fun getEntityFieldsWithTranslationsWithLanguage(uuid: String, label: String, type: TranslatableEntityFieldTypeModel, lang: String): ResultModel<GenericArrayResponse<TranslationKeyValuePair>> =
        target.translationWebTarget
            .path(String.format(GET_ENTITY_FIELD_TRANSLATION_WITH_LANGUAGE_API, uuid, label, type.name, lang))
            .also { logger.debug("Calling web target '{}'", it.uri) }
            .request(MediaType.APPLICATION_JSON)
            .get(object : GenericType<ResultModel<GenericArrayResponse<TranslationKeyValuePair>>>() {})

    //endregion

    //region Bulk create/update

    override fun createOrUpdateTranslatableEntityWithDependencies(type: TranslatableEntityFieldTypeModel, request: TranslationAggregationByEntityRequestModel): ResultModel<TranslationAggregationByEntityResponseModel> =
        target.translationWebTarget
            .path(String.format(CREATE_OR_UPDATE_ENTITY_FIELD_TRANSLATION_WITH_LANGUAGE_API, type.name))
            .also { logger.debug("Calling web target '{}'", it.uri) }
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(request, MediaType.APPLICATION_JSON), object : GenericType<ResultModel<TranslationAggregationByEntityResponseModel>>() {})

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslationApiClientImpl::class.java)

        private const val ENTITY_CREATE_API = "/entity"
        private const val ENTITY_FIELD_CREATE_API = "/entity/field"
        private const val ENTITY_FIELD_TRANSLATION_CREATE_API = "/entity/field/translation"
        private const val UPDATE_ENTITY_FIELD_TRANSLATION_CREATE_API = "/entity/%s/%s/field/%s/%s/translation"
        private const val GET_ENTITY_FIELD_TRANSLATION_API = "/entity/%s/%s/{type}"
        private const val GET_ENTITY_FIELD_TRANSLATION_WITH_LANGUAGE_API = "/entity/%s/%s/%s/%s"
        private const val CREATE_OR_UPDATE_ENTITY_FIELD_TRANSLATION_WITH_LANGUAGE_API = "/entity/field/%s/translation/bulk"
    }
}