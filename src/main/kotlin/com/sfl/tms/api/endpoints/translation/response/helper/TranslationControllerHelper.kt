package com.sfl.tms.api.endpoints.translation.response.helper

import com.sfl.tms.api.endpoints.translation.request.aggregation.TranslationKeyValuePair
import com.sfl.tms.api.endpoints.translation.request.aggregation.multiple.TranslationAggregationByEntityRequestModel
import com.sfl.tms.api.endpoints.translation.request.aggregation.multiple.TranslationAggregationByLanguage
import com.sfl.tms.api.endpoints.translation.response.aggregation.multiple.TranslationAggregationByEntityResponseModel
import com.sfl.tms.api.endpoints.translation.response.helper.exception.TranslatableEntityMissingException
import com.sfl.tms.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import com.sfl.tms.service.translatable.translation.TranslatableEntityFieldTranslationService
import com.sfl.tms.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto
import com.sfl.tms.service.translatable.translation.exception.TranslatableFieldTranslationNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * User: Vazgen Danielyan
 * Date: 1/19/19
 * Time: 12:43 AM
 */
@Component
class TranslationControllerHelper {

    //region Injection

    @Autowired
    private lateinit var languageService: LanguageService

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Autowired
    private lateinit var translatableEntityFieldTranslationService: TranslatableEntityFieldTranslationService

    //endregion

    @Transactional
    fun createOrUpdateTranslatableEntityWithDependencies(request: TranslationAggregationByEntityRequestModel, type: TranslatableEntityFieldType): TranslationAggregationByEntityResponseModel = request.let {

        logger.trace("Create or update TranslatableEntity with dependencies using provided request - {}", request)

        val entity = try {
            translatableEntityService.getByUuidAndLabel(it.uuid, it.label)
        } catch (e: TranslatableEntityNotFoundException) {
            if (it.name == null) {
                logger.error("TranslatableEntity name missing when trying to perform create.")
                throw TranslatableEntityMissingException()
            } else {
                translatableEntityService.create(TranslatableEntityDto(it.uuid, it.label, it.name))
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
                    translatableEntityFieldService.getByKeyAndTypeAndEntity(it.key, type, entity.uuid, entity.label)
                } catch (e: TranslatableEntityFieldNotFoundException) {
                    translatableEntityFieldService.create(TranslatableEntityFieldDto(it.key, type, entity.uuid, entity.label))
                }

                try {
                    translatableEntityFieldTranslationService.getByFieldAndLanguage(it.key, type, entity.uuid, entity.label, language.lang)
                            .also { translatableEntityFieldTranslationService.updateValue(TranslatableEntityFieldTranslationDto(it.field.key, type, it.value, entity.uuid, entity.label, language.lang)) }
                } catch (e: TranslatableFieldTranslationNotFoundException) {
                    translatableEntityFieldTranslationService.create(TranslatableEntityFieldTranslationDto(it.key, type, it.value, entity.uuid, entity.label, language.lang))
                }
            }
        }

        TranslationAggregationByEntityResponseModel(it.uuid, it.label, it.languages.map { TranslationAggregationByLanguage(it.lang, it.keys.map { TranslationKeyValuePair(it.key, it.value) }) })
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslationControllerHelper::class.java)
    }

}