package com.sfl.tms.rest.common.communicator.translation

import com.sfl.tms.rest.common.communicator.translation.model.TranslatableEntityFieldTypeModel
import com.sfl.tms.rest.common.communicator.translation.request.aggregation.multiple.TranslationAggregationByEntityRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.entity.TranslatableEntityCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.field.TranslatableEntityFieldCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.translation.TranslatableEntityFieldTranslationCreateRequestModel
import com.sfl.tms.rest.common.communicator.translation.request.translation.TranslatableEntityFieldTranslationUpdateRequestModel
import com.sfl.tms.rest.common.communicator.translation.response.aggregation.multiple.TranslationAggregationByEntityResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.entity.TranslatableEntityCreateResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.field.TranslatableEntityFieldCreateResponseModel
import com.sfl.tms.rest.common.communicator.translation.response.translation.TranslatableEntityFieldTranslationResponseModel
import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.ResultModel
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * User: Vazgen Danielyan
 * Date: 1/26/19
 * Time: 11:01 PM
 */
@CrossOrigin(origins = ["*"])
@RequestMapping("/translation", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
interface TranslationCommunicator {

    //region Entity

    @ApiOperation(value = "Create translatable entity", response = TranslatableEntityCreateResponseModel::class)
    @RequestMapping(value = ["/entity"], method = [RequestMethod.POST])
    fun createTranslatableEntity(@RequestBody request: TranslatableEntityCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>>

    //endregion

    //region Field

    @ApiOperation(value = "Create translatable entity field", response = TranslatableEntityFieldCreateResponseModel::class)
    @RequestMapping(value = ["/entity/field"], method = [RequestMethod.POST])
    fun createTranslatableEntityField(@RequestBody request: TranslatableEntityFieldCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>>

    //endregion

    //region Create translatable entity field translation

    @ApiOperation(value = "Create translatable entity field translation", response = TranslatableEntityFieldTranslationResponseModel::class)
    @RequestMapping(value = ["/entity/field/translation"], method = [RequestMethod.POST])
    fun createTranslatableEntityFieldTranslation(@RequestBody request: TranslatableEntityFieldTranslationCreateRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>>

    //endregion

    //region Update translatable entity field translation

    @ApiOperation(value = "Update translatable entity field translation", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/field/{key}/{type}/translation"], method = [RequestMethod.PATCH])
    fun updateTranslatableEntityFieldTranslation(
            @PathVariable("uuid") uuid: String,
            @PathVariable("label") label: String,
            @PathVariable("key") key: String,
            @PathVariable("type") type: TranslatableEntityFieldTypeModel,
            @RequestBody request: List<TranslatableEntityFieldTranslationUpdateRequestModel>): ResponseEntity<ResultModel<out AbstractApiModel>>

    //endregion

    //region Get entity fields with translations

    @ApiOperation(value = "Get translatable entity field's with translation's for provided filters", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/{type}"], method = [RequestMethod.GET])
    fun getEntityFieldsWithTranslations(@PathVariable("uuid") uuid: String, @PathVariable("label") label: String, @PathVariable("type") type: TranslatableEntityFieldTypeModel): ResponseEntity<ResultModel<out AbstractApiModel>>

    @ApiOperation(value = "Get translatable entity field's with translation's for provided filters", response = List::class)
    @RequestMapping(value = ["/entity/{uuid}/{label}/{type}/{lang}"], method = [RequestMethod.GET])
    fun getEntityFieldsWithTranslationsWithLanguage(@PathVariable("uuid") uuid: String, @PathVariable("label") label: String, @PathVariable("type") type: TranslatableEntityFieldTypeModel, @PathVariable("lang") lang: String): ResponseEntity<ResultModel<out AbstractApiModel>>

    //endregion

    //region Bulk create/update

    @ApiOperation(value = "Create/update translatable entity, field, translations", response = TranslationAggregationByEntityResponseModel::class)
    @RequestMapping(value = ["/entity/field/{type}/translation/bulk"], method = [RequestMethod.POST])
    fun createOrUpdateTranslatableEntityWithDependencies(@PathVariable("type") type: TranslatableEntityFieldTypeModel, @RequestBody request: TranslationAggregationByEntityRequestModel): ResponseEntity<ResultModel<out AbstractApiModel>>

    //endregion

}