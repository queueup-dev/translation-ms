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
import com.sfl.tms.api.endpoints.translation.request.statics.TranslatableStaticCreateRequestModel
import com.sfl.tms.api.endpoints.translation.request.statics.TranslatableStaticUpdateRequestModel
import com.sfl.tms.api.endpoints.translation.response.entity.TranslatableEntityCreateResponseModel
import com.sfl.tms.api.endpoints.translation.response.field.TranslatableEntityFieldCreateResponseModel
import com.sfl.tms.api.endpoints.translation.response.statics.TranslatableStaticResponseModel
import com.sfl.tms.api.endpoints.translation.response.statics.TranslatableStaticsPageResponseModel
import com.sfl.tms.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityExistsByUuidException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.service.translatablestatic.TranslatableStaticService
import com.sfl.tms.service.translatablestatic.dto.TranslatableStaticDto
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticExistException
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndEntityUuidException
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndLanguageLangException
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
@RestController
@RequestMapping("/translation")
class TranslationController {

    //region Injection

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Autowired
    private lateinit var translatableStaticService: TranslatableStaticService

    //endregion

    //region Entity with translations

    @ApiOperation(value = "Create translatable entity", response = TranslatableEntityCreateResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/entity"], method = [RequestMethod.POST])
    fun createTranslatableEntity(@RequestBody request: TranslatableEntityCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Creating new TranslatableEntity for provided request - {} ", it) }
                .let { translatableEntityService.create(TranslatableEntityDto(it.uuid, it.name)) }
                .let { created(TranslatableEntityCreateResponseModel(it.uuid, it.name)) }
                .also { logger.debug("Successfully created TranslatableEntity for provided request - {} ", request) }
    } catch (e: TranslatableEntityExistsByUuidException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Field with translations

    @ApiOperation(value = "Create translatable entity field", response = TranslatableEntityFieldCreateResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/entity/field"], method = [RequestMethod.POST])
    fun createTranslatableEntityField(@RequestBody request: TranslatableEntityFieldCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Creating new translatable entity field for provided request - {} ", it) }
                .let { translatableEntityFieldService.create(TranslatableEntityFieldDto(it.entityUuid, it.fieldName)) }
                .let { created(TranslatableEntityFieldCreateResponseModel(it.entity.uuid, it.name)) }
                .also { logger.debug("Successfully created translatable entity field for provided request - {} ", request) }
    } catch (e: TranslatableEntityFieldExistsForTranslatableEntityException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_ENTITY_FIELD_EXISTS_BY_UUID_EXCEPTION)
    }

    //endregion

    //region Static translations

    @ApiOperation(value = "Create translatable static for entity", response = TranslatableStaticResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/static"], method = [RequestMethod.POST])
    fun createTranslatableStatic(@RequestBody request: TranslatableStaticCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Creating new TranslatableStatic for provided request - {} ", it) }
                .let { translatableStaticService.create(TranslatableStaticDto(it.key, it.entityUuid, it.value, it.lang)) }
                .let { created(TranslatableStaticResponseModel(it.key, it.entity.uuid, it.value, it.language.lang)) }
                .also { logger.debug("Successfully created TranslatableStatic for provided request - {} ", request) }
    } catch (e: LanguageNotFoundByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    } catch (e: TranslatableStaticExistException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_STATIC_EXIST_EXCEPTION)
    }

    @ApiOperation(value = "Update translatable static for entity", response = List::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/static/{key}"], method = [RequestMethod.PATCH])
    fun updateTranslatableStatic(@PathVariable("key") key: String, @RequestBody request: List<TranslatableStaticUpdateRequestModel>): ResponseEntity<ResultModel<out AbstractApiModel>> = try {
        request
                .also { logger.trace("Updating TranslatableStatic for provided request - {} ", it) }
                .also {
                    it.map { it.validateRequiredFields() }.flatten().distinct().let {
                        if (it.isNotEmpty()) {
                            return ResponseEntity(ResultModel<ErrorType>(it), HttpStatus.BAD_REQUEST)
                        }
                    }
                }
                .map { translatableStaticService.updateValue(TranslatableStaticDto(key, it.entityUuid, it.value, it.lang)) }
                .map { TranslatableStaticResponseModel(it.key, it.entity.uuid, it.value, it.language.lang) }
                .let { ok(object : AbstractApiResponseModel, ArrayList<TranslatableStaticResponseModel>(it) {}) }
                .also { logger.debug("Successfully updated TranslatableStatic for provided request - {} ", request) }
    } catch (e: LanguageNotFoundByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    } catch (e: TranslatableStaticNotFoundByKeyAndLanguageLangException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_STATIC_NOT_FOUND_BY_KEY_AND_LANGUAGE_LANG_EXCEPTION)
    }

    @ApiOperation(value = "Get translatable static for key, entity and language", response = TranslatableStaticResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/static"], method = [RequestMethod.GET])
    fun getStaticTranslation(@RequestParam("key") key: String, @RequestParam("uuid", required = true) uuid: String, @RequestParam("lang", required = false) lang: String?) = try {
        key
                .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, entity uuid - {} and lang - {} ", it, uuid, lang) }
                .let {
                    if (lang == null) {
                        translatableStaticService.getByKeyAndEntityUuid(it, uuid)
                                .map { TranslatableStaticResponseModel(it.key, it.entity.uuid, it.value, it.language.lang) }
                                .groupBy { it.lang }
                                .let { ok(TranslatableStaticsPageResponseModel(it)) }
                                .also { logger.debug("Retrieved TranslatableStatic for provided key - {}, lang - {} ", key, lang) }

                    } else {
                        translatableStaticService.getByKeyAndEntityUuidAndLanguageLang(it, uuid, lang)
                                .let { ok(TranslatableStaticResponseModel(it.key, it.entity.uuid, it.value, it.language.lang)) }
                                .also { logger.debug("Retrieved TranslatableStatic for provided key - {}", key) }
                    }
                }
    } catch (e: TranslatableStaticNotFoundByKeyAndEntityUuidException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_STATIC_NOT_FOUND_BY_KEY_EXCEPTION)
    } catch (e: LanguageNotFoundByLangException) {
        internal(TranslationControllerErrorType.LANGUAGE_NOT_FOUND_BY_LANG_EXCEPTION)
    } catch (e: TranslatableStaticNotFoundByKeyAndLanguageLangException) {
        internal(TranslationControllerErrorType.TRANSLATABLE_STATIC_NOT_FOUND_BY_KEY_AND_LANGUAGE_LANG_EXCEPTION)
    }

    @ApiOperation(value = "Search translatable static by key and language", response = TranslatableStaticResponseModel::class)
    @ValidateActionRequest
    @RequestMapping(value = ["/static/search"], method = [RequestMethod.GET])
    fun searchStaticTranslations(@RequestParam("term", required = false) term: String?, @RequestParam("lang", required = false) lang: String?, @RequestParam("page", required = false) page: Int?) = term
            .also { logger.trace("Retrieving TranslatableStatic list for provided term - {}, page - {} ", it, page) }
            .let { translatableStaticService.search(it, lang, page) }
            .map { TranslatableStaticResponseModel(it.key, it.entity.uuid, it.value, it.language.lang) }
            .groupBy { it.lang }
            .let { ok(TranslatableStaticsPageResponseModel(it)) }
            .also { logger.debug("Retrieved TranslatableStatic list for provided term - {}, page - {} ", term, page) }

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslationController::class.java)
    }

}