package com.sfl.tms.rest.server.translation

import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.core.service.language.LanguageService
import com.sfl.tms.core.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.core.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.core.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityExistsException
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityNotFoundException
import com.sfl.tms.core.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.core.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import com.sfl.tms.core.service.translatable.translation.TranslatableEntityFieldTranslationService
import com.sfl.tms.core.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto
import com.sfl.tms.core.service.translatable.translation.exception.TranslatableFieldTranslationExistException
import com.sfl.tms.core.service.translatable.translation.exception.TranslatableFieldTranslationNotFoundException
import com.sfl.tms.rest.common.annotations.ValidateActionRequest
import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.communicator.translation.model.TranslatableEntityFieldTypeModel
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.TranslationKeyValuePair
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple.TranslationAggregationByEntityRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple.TranslationAggregationByLanguage
import com.sfl.tms.rest.common.communicator.translation.request.entity.TranslatableEntityCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.field.TranslatableEntityFieldCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.translation.TranslatableEntityFieldTranslationCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.translation.TranslatableEntityFieldTranslationUpdateRequestModel
import com.sfl.tms.rest.common.communicator.translation.response.aggregation.multiple.TranslationAggregationByEntityResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.aggregation.single.TranslationAggregationByKey
import com.sfl.tms.rest.common.communicator.translation.response.aggregation.single.TranslationLanguageValuePair
import com.sfl.tms.rest.common.communicator.translation.response.entity.TranslatableEntityCreateResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.field.TranslatableEntityFieldCreateResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.translation.TranslatableEntityFieldTranslationResponseModel
import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.ResultModel
import com.sfl.tms.rest.common.model.error.ErrorType
import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel
import com.sfl.tms.rest.server.AbstractBaseController
import com.sfl.tms.rest.server.translation.exception.TranslatableEntityMissingException
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 2:05 PM
 */
@RestController
@RequestMapping("/translation")
class TranslationController : AbstractBaseController() {

    //region Injection

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Autowired
    private lateinit var translatableEntityFieldTranslationService: TranslatableEntityFieldTranslationService

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    //region Create entity

    @ValidateActionRequest
    @ApiOperation(value = "Create translatable entity", response = TranslatableEntityCreateResponseModel::class)
    @RequestMapping(value = ["/entity"], method = [RequestMethod.POST])
    fun createTranslatableEntity(@RequestBody request: TranslatableEntityCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
            .also { logger.trace("Creating new TranslatableEntity for provided request - {} ", it) }
            .let { translatableEntityService.create(TranslatableEntityDto(it.uuid, it.label, it.name)) }
            .also { logger.trace("Copying static TranslatableEntityField for provided request - {} ", it) }
            .also { translatableEntityFieldService.copyStatics(it.uuid, it.label) }
            .let { created(TranslatableEntityCreateResponseModel(it.uuid, it.label, it.name)) }
            .also { logger.debug("Successfully created TranslatableEntity for provided request - {} ", request) }
    } catch (e: TranslatableEntityExistsException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Create field

    @ValidateActionRequest
    @ApiOperation(value = "Create translatable entity field", response = TranslatableEntityFieldCreateResponseModel::class)
    @RequestMapping(value = ["/entity/field"], method = [RequestMethod.POST])
    fun createTranslatableEntityField(@RequestBody request: TranslatableEntityFieldCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
            .also { logger.trace("Creating new translatable entity field for provided request - {} ", it) }
            .let { translatableEntityFieldService.create(TranslatableEntityFieldDto(it.key, TranslatableEntityFieldType.valueOf(it.type.name), it.uuid, it.label)) }
            .let { created(TranslatableEntityFieldCreateResponseModel(it.entity.uuid, it.key)) }
            .also { logger.debug("Successfully created translatable entity field for provided request - {} ", request) }
    } catch (e: TranslatableEntityNotFoundException) {
        notFound(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION)
    } catch (e: TranslatableEntityFieldExistsForTranslatableEntityException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Create translatable entity field translation

    @ValidateActionRequest
    @ApiOperation(value = "Create translatable entity field translation", response = TranslatableEntityFieldTranslationResponseModel::class)
    @RequestMapping(value = ["/entity/field/translation"], method = [RequestMethod.POST])
    fun createTranslatableEntityFieldTranslation(@RequestBody request: TranslatableEntityFieldTranslationCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
            .also { logger.trace("Creating new TranslatableEntityFieldTranslation for provided request - {} ", it) }
            .let { translatableEntityFieldTranslationService.create(TranslatableEntityFieldTranslationDto(it.key, TranslatableEntityFieldType.valueOf(it.type.name), it.value, it.uuid, it.label, it.lang)) }
            .let { created(TranslatableEntityFieldTranslationResponseModel(it.field.key, it.value, it.field.entity.uuid, it.field.entity.label, it.language.lang)) }
            .also { logger.debug("Successfully created TranslatableEntityFieldTranslation for provided request - {} ", request) }
    } catch (e: LanguageNotFoundByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    } catch (e: TranslatableFieldTranslationExistException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_EXIST_EXCEPTION)
    } catch (e: TranslatableEntityNotFoundException) {
        notFound(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION)
    } catch (e: TranslatableEntityFieldExistsForTranslatableEntityException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Update translatable entity field translation

    @ValidateActionRequest
    @ApiOperation(value = "Update translatable entity field translation", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/field/{key}/{type}/translation"], method = [RequestMethod.PUT])
    fun updateTranslatableEntityFieldTranslation(
        @PathVariable("uuid") uuid: String,
        @PathVariable("label") label: String,
        @PathVariable("key") key: String,
        @PathVariable("type") type: TranslatableEntityFieldTypeModel,
        @RequestBody request: List<TranslatableEntityFieldTranslationUpdateRequestModel>
    ): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
            .also { logger.trace("Updating TranslatableEntityFieldTranslation for provided request - {} ", it) }
            .also {
                it
                    .map { it.validateRequiredFields() }
                    .flatten()
                    .distinct()
                    .let {
                        if (it.isNotEmpty()) {
                            return ResponseEntity(ResultModel<ErrorType>(it), HttpStatus.BAD_REQUEST)
                        }
                    }
            }
            .map { translatableEntityFieldTranslationService.updateValue(TranslatableEntityFieldTranslationDto(key, TranslatableEntityFieldType.valueOf(type.name), it.value, uuid, label, it.lang)) }
            .map { TranslatableEntityFieldTranslationResponseModel(key, it.value, uuid, label, it.language.lang) }
            .let { ok(object : AbstractApiResponseModel, ArrayList<TranslatableEntityFieldTranslationResponseModel>(it) {}) }
            .also { logger.debug("Successfully updated TranslatableEntityFieldTranslation for provided request - {} ", request) }
    } catch (e: LanguageNotFoundByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    } catch (e: TranslatableEntityFieldNotFoundException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_NOT_FOUND_EXCEPTION)
    } catch (e: TranslatableFieldTranslationNotFoundException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_NOT_FOUND_EXCEPTION)
    }

    //endregion

    //region Get entity fields with translations

    @ApiOperation(value = "Get translatable entity field's with translation's for provided filters", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/{type}"], method = [RequestMethod.GET])
    fun getEntityFieldsWithTranslations(@PathVariable("uuid") uuid: String, @PathVariable("label") label: String, @PathVariable("type") type: TranslatableEntityFieldTypeModel): ResponseEntity<ResultModel<out AbstractApiModel>> =
        try {
            ok(object :
                AbstractApiResponseModel,
                ArrayList<TranslationAggregationByKey>(
                    translatableEntityService
                        .getByUuidAndLabel(uuid, label)
                        .fields
                        .filter { it.type == TranslatableEntityFieldType.valueOf(type.name) }
                        .map {
                            TranslationAggregationByKey(
                                it.key,
                                it.translations
                                    .map { TranslationLanguageValuePair(it.language.lang, it.value) }
                            )
                        }
                ) {}
            )
        } catch (e: TranslatableEntityNotFoundException) {
            notFound(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION)
        }

    @ApiOperation(value = "Get translatable entity field's with translation's for provided filters", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/{type}/{lang}"], method = [RequestMethod.GET])
    fun getEntityFieldsWithTranslationsWithLanguage(@PathVariable("uuid") uuid: String, @PathVariable("label") label: String, @PathVariable("type") type: TranslatableEntityFieldTypeModel, @PathVariable("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>> =
        try {
            ok(object :
                AbstractApiResponseModel,
                ArrayList<TranslationKeyValuePair>(
                    translatableEntityService
                        .getByUuidAndLabel(uuid, label)
                        .fields
                        .filter { it.type == TranslatableEntityFieldType.valueOf(type.name) }
                        .map { TranslationKeyValuePair(it.key, it.translations.filter { it.language.lang == lang }.map { it.value }.first()) }
                ) {}
            )
        } catch (e: TranslatableEntityNotFoundException) {
            notFound(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION)
        }

    @ApiOperation(value = "Get translatable entity field's with translation's for provided filters and languages", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/{type}/languages/{languages}"], method = [RequestMethod.GET])
    fun getEntityFieldsWithTranslationsForLanguages(@PathVariable("uuid") uuid: String, @PathVariable("label") label: String, @PathVariable("type") type: TranslatableEntityFieldTypeModel, @PathVariable("languages") lang: List<String>): ResponseEntity<ResultModel<out AbstractApiModel>> =
        try {
            ok(object :
                AbstractApiResponseModel,
                ArrayList<TranslationKeyValuePair>(
                    translatableEntityService
                        .getByUuidAndLabel(uuid, label)
                        .fields
                        .filter { it.type == TranslatableEntityFieldType.valueOf(type.name) }
                        .map { TranslationKeyValuePair(it.key, it.translations.filter { lang.contains(it.language.lang) }.map { it.value }.first()) }
                ) {}
            )
        } catch (e: TranslatableEntityNotFoundException) {
            notFound(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION)
        }

    //endregion

    //region Bulk create/update

    @ValidateActionRequest
    @ApiOperation(value = "Create/update translatable entity, field, translations", response = TranslationAggregationByEntityResponseModel::class)
    @RequestMapping(value = ["/entity/field/{type}/translation/bulk"], method = [RequestMethod.POST])
    fun createOrUpdateTranslatableEntityWithDependencies(@PathVariable("type") type: TranslatableEntityFieldTypeModel, @RequestBody request: TranslationAggregationByEntityRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> =
        try {
            created(createOrUpdateTranslatableEntityWithDependencies(request, type))
        } catch (e: TranslatableEntityMissingException) {
            internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NAME_MISSING)
        } catch (e: LanguageNotFoundByLangException) {
            internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
        } catch (e: TranslatableFieldTranslationExistException) {
            internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_EXIST_EXCEPTION)
        } catch (e: TranslatableEntityNotFoundException) {
            notFound(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_EXCEPTION)
        } catch (e: TranslatableEntityFieldExistsForTranslatableEntityException) {
            internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION)
        }

    //endregion

    //region Utility methods

    @Transactional
    fun createOrUpdateTranslatableEntityWithDependencies(request: TranslationAggregationByEntityRequestModel, type: TranslatableEntityFieldTypeModel): TranslationAggregationByEntityResponseModel = request.let {

        logger.trace("Create or update TranslatableEntity with dependencies using provided request - {}", request)

        val entity = try {
            translatableEntityService.getByUuidAndLabel(it.uuid, it.label)
        } catch (e: TranslatableEntityNotFoundException) {
            if (it.name == null) {
                logger.error("TranslatableEntity name missing when trying to perform create.")
                throw TranslatableEntityMissingException()
            } else {
                translatableEntityService.create(TranslatableEntityDto(it.uuid, it.label, it.name!!))
            }
        }

        it.languages.forEach {

            val language = try {
                languageService.getByLang(it.lang)
            } catch (e: LanguageNotFoundByLangException) {
                languageService.create(it.lang)
            }

            it.keys.forEach {
                try {
                    translatableEntityFieldService.getByKeyAndTypeAndEntity(it.key, TranslatableEntityFieldType.valueOf(type.name), entity.uuid, entity.label)
                } catch (e: TranslatableEntityFieldNotFoundException) {
                    translatableEntityFieldService.create(TranslatableEntityFieldDto(it.key, TranslatableEntityFieldType.valueOf(type.name), entity.uuid, entity.label))
                }

                val value = it.value

                try {
                    translatableEntityFieldTranslationService.getByFieldAndLanguage(it.key, TranslatableEntityFieldType.valueOf(type.name), entity.uuid, entity.label, language.lang).also {
                        translatableEntityFieldTranslationService.updateValue(
                            TranslatableEntityFieldTranslationDto(
                                it.field.key,
                                TranslatableEntityFieldType.valueOf(type.name),
                                value,
                                entity.uuid,
                                entity.label,
                                language.lang
                            )
                        )
                    }
                } catch (e: TranslatableFieldTranslationNotFoundException) {
                    translatableEntityFieldTranslationService.create(TranslatableEntityFieldTranslationDto(it.key, TranslatableEntityFieldType.valueOf(type.name), it.value, entity.uuid, entity.label, language.lang))
                }
            }
        }

        TranslationAggregationByEntityResponseModel(it.uuid, it.label, it.languages.map { TranslationAggregationByLanguage(it.lang, it.keys.map { TranslationKeyValuePair(it.key, it.value) }) })
    }

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslationController::class.java)
    }

}