package com.sfl.tms.api.endpoints.translation

import com.sfl.tms.api.common.annotations.ValidateActionRequest
import com.sfl.tms.api.common.model.AbstractApiModel
import com.sfl.tms.api.common.model.ResultModel
import com.sfl.tms.api.common.model.error.ErrorType
import com.sfl.tms.api.common.model.response.AbstractApiResponseModel
import com.sfl.tms.api.endpoints.AbstractBaseController.Companion.created
import com.sfl.tms.api.endpoints.AbstractBaseController.Companion.internal
import com.sfl.tms.api.endpoints.AbstractBaseController.Companion.ok
import com.sfl.tms.api.endpoints.translation.error.TranslationControllerErrorType
import com.sfl.tms.api.endpoints.translation.request.entity.TranslatableEntityCreateRequestModel
import com.sfl.tms.api.endpoints.translation.request.field.TranslatableEntityFieldCreateRequestModel
import com.sfl.tms.api.endpoints.translation.request.translation.TranslatableEntityFieldTranslationCreateRequestModel
import com.sfl.tms.api.endpoints.translation.request.translation.TranslatableEntityFieldTranslationUpdateRequestModel
import com.sfl.tms.api.endpoints.translation.response.entity.TranslatableEntityCreateResponseModel
import com.sfl.tms.api.endpoints.translation.response.field.TranslatableEntityFieldCreateResponseModel
import com.sfl.tms.api.endpoints.translation.response.translation.TranslatableEntityFieldTranslationResponseModel
import com.sfl.tms.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityExistsByUuidAndLabelException
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidAndLabelException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import com.sfl.tms.service.translatable.translation.TranslatableEntityFieldTranslationService
import com.sfl.tms.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto
import com.sfl.tms.service.translatable.translation.exception.TranslatableFieldTranslationExistException
import com.sfl.tms.service.translatable.translation.exception.TranslatableFieldTranslationNotFoundException
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 2:05 PM
 */
@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/translation")
class TranslationController {

    //region Injection

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Autowired
    private lateinit var translatableEntityFieldTranslationService: TranslatableEntityFieldTranslationService

    //endregion

    //region Entity

    @ApiOperation(value = "Create translatable entity", response = TranslatableEntityCreateResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/entity"], method = [RequestMethod.POST])
    fun createTranslatableEntity(@RequestBody request: TranslatableEntityCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Creating new TranslatableEntity for provided request - {} ", it) }
                .let { translatableEntityService.create(TranslatableEntityDto(it.uuid, it.label, it.name)) }
                .let { created(TranslatableEntityCreateResponseModel(it.uuid, it.label, it.name)) }
                .also { logger.debug("Successfully created TranslatableEntity for provided request - {} ", request) }
    } catch (e: TranslatableEntityExistsByUuidAndLabelException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Field

    @ApiOperation(value = "Create translatable entity field", response = TranslatableEntityFieldCreateResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/entity/field"], method = [RequestMethod.POST])
    fun createTranslatableEntityField(@RequestBody request: TranslatableEntityFieldCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Creating new translatable entity field for provided request - {} ", it) }
                .let { translatableEntityFieldService.create(TranslatableEntityFieldDto(it.key, it.uuid, it.label)) }
                .let { created(TranslatableEntityFieldCreateResponseModel(it.entity.uuid, it.key)) }
                .also { logger.debug("Successfully created translatable entity field for provided request - {} ", request) }
    } catch (e: TranslatableEntityNotFoundByUuidAndLabelException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_BY_UUID_EXCEPTION)
    } catch (e: TranslatableEntityFieldExistsForTranslatableEntityException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Create translatable entity field translation

    @ApiOperation(value = "Create translatable entity field translation", response = TranslatableEntityFieldTranslationResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/entity/field/translation"], method = [RequestMethod.POST])
    fun createTranslatableEntityFieldTranslation(@RequestBody request: TranslatableEntityFieldTranslationCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Creating new TranslatableEntityFieldTranslation for provided request - {} ", it) }
                .let { translatableEntityFieldTranslationService.create(TranslatableEntityFieldTranslationDto(it.key, it.value, it.uuid, it.label, it.lang)) }
                .let { created(TranslatableEntityFieldTranslationResponseModel(it.field.key, it.value, it.field.entity.uuid, it.field.entity.label, it.language.lang)) }
                .also { logger.debug("Successfully created TranslatableEntityFieldTranslation for provided request - {} ", request) }
    } catch (e: LanguageNotFoundByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    } catch (e: TranslatableFieldTranslationExistException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_TRANSLATION_EXIST_EXCEPTION)
    } catch (e: TranslatableEntityNotFoundByUuidAndLabelException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_NOT_FOUND_BY_UUID_EXCEPTION)
    } catch (e: TranslatableEntityFieldExistsForTranslatableEntityException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Update translatable entity field translation

    @ApiOperation(value = "Update translatable entity field translation", response = List::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/entity/{uuid}/{label}/field/{key}/translation"], method = [RequestMethod.PATCH])
    fun updateTranslatableEntityFieldTranslation(
            @PathVariable("uuid") uuid: String,
            @PathVariable("label") label: String,
            @PathVariable("key") key: String,
            @RequestBody request: List<TranslatableEntityFieldTranslationUpdateRequestModel>): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
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
                .map { translatableEntityFieldTranslationService.updateValue(TranslatableEntityFieldTranslationDto(key, it.value, uuid, label, it.lang)) }
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

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslationController::class.java)
    }

}