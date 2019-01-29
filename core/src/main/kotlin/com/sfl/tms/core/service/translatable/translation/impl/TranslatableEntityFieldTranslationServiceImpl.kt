package com.sfl.tms.core.service.translatable.translation.impl

import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldTranslation
import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.core.persistence.translatable.TranslatableEntityFieldTranslationRepository
import com.sfl.tms.core.service.language.LanguageService
import com.sfl.tms.core.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.core.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.core.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import com.sfl.tms.core.service.translatable.translation.TranslatableEntityFieldTranslationService
import com.sfl.tms.core.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto
import com.sfl.tms.core.service.translatable.translation.exception.TranslatableFieldTranslationExistException
import com.sfl.tms.core.service.translatable.translation.exception.TranslatableFieldTranslationNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: Vazgen Danielyan
 * Date: 1/15/19
 * Time: 6:31 PM
 */
@Service
class TranslatableEntityFieldTranslationServiceImpl : TranslatableEntityFieldTranslationService {

    //region Injection

    @Autowired
    private lateinit var translatableEntityFieldTranslationRepository: TranslatableEntityFieldTranslationRepository

    @Autowired
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    //region findByFieldAndLanguage

    @Throws(TranslatableEntityFieldNotFoundException::class, LanguageNotFoundByLangException::class)
    @Transactional(readOnly = true)
    override fun findByFieldAndLanguage(key: String, type: TranslatableEntityFieldType, uuid: String, label: String, lang: String): TranslatableEntityFieldTranslation? = key
            .also { logger.trace("Retrieving TranslatableEntityFieldTranslation for provided key - {}, type - {}, uuid - {}, label - {} and lang - {}", key, type, uuid, label, lang) }
            .let { translatableEntityFieldTranslationRepository.findByFieldAndLanguage(translatableEntityFieldService.getByKeyAndTypeAndEntity(key, type, uuid, label), languageService.getByLang(lang)) }
            .also { logger.debug("Retrieved TranslatableEntityFieldTranslation for provided key - {}, type - {}, uuid - {}, label - {} and lang - {}", key, type, uuid, label, lang) }

    //endregion

    //region getByFieldAndLanguage

    @Throws(TranslatableFieldTranslationNotFoundException::class)
    @Transactional(readOnly = true)
    override fun getByFieldAndLanguage(key: String, type: TranslatableEntityFieldType, uuid: String, label: String, lang: String): TranslatableEntityFieldTranslation = key
            .also { logger.trace("Retrieving TranslatableEntityFieldTranslation for provided key - {}, type - {}, uuid - {}, label - {} and lang - {}", it, type, uuid, label, lang) }
            .let {
                findByFieldAndLanguage(key, type, uuid, label, lang).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableEntityFieldTranslation for key - {}, type - {}, uuid - {}, label - {} and lang - {}", key, type, uuid, label, lang)
                        throw TranslatableFieldTranslationNotFoundException(key, uuid, label, lang)
                    }
                    logger.debug("Retrieved TranslatableEntityFieldTranslation for provided key - {}, type - {}, uuid - {}, label - {} and lang - {}", key, type, uuid, label, lang)
                    it
                }
            }

    //endregion

    //region getByKeyAndTypeAndEntity

    @Transactional(readOnly = true)
    override fun getByKeyAndEntity(key: String, type: TranslatableEntityFieldType, uuid: String, label: String): List<TranslatableEntityFieldTranslation> = key
            .also { logger.trace("Retrieving TranslatableEntityFieldTranslation for provided key - {}, type - {}, uuid - {} and label - {}", it, type, uuid, label) }
            .let { translatableEntityFieldService.getByKeyAndTypeAndEntity(key, type, uuid, label).let { translatableEntityFieldTranslationRepository.findByField(it) } }
            .also { logger.debug("Retrieved TranslatableEntityFieldTranslation for provided key - {}, type - {}, uuid - {} and label - {}", key, type, uuid, label) }

    //endregion

    //region create

    @Throws(TranslatableFieldTranslationExistException::class)
    @Transactional
    override fun create(dto: TranslatableEntityFieldTranslationDto): TranslatableEntityFieldTranslation {
        logger.trace("Creating new TranslatableEntityFieldTranslation for provided dto - {}", dto)

        val language = languageService.getByLang(dto.lang)

        val field = translatableEntityFieldService.findByKeyAndTypeAndEntity(dto.key, dto.type, dto.uuid, dto.label).let {
            it ?: translatableEntityFieldService.create(TranslatableEntityFieldDto(dto.key, dto.type, dto.uuid, dto.label))
        }

        val translation = findByFieldAndLanguage(dto.key, dto.type, dto.uuid, dto.label, dto.lang)

        if (translation != null) {
            logger.error("Unable to create new TranslatableEntityFieldTranslation for provided dto - {}. Already exists.", dto)
            throw TranslatableFieldTranslationExistException(dto.key, dto.uuid, dto.label, dto.lang)
        }

        // Create new translation
        return TranslatableEntityFieldTranslation()
                .apply { this.value = dto.value }
                .apply { this.field = field }
                .apply { this.language = language }
                .let { translatableEntityFieldTranslationRepository.save(it) }
                .also { logger.debug("Successfully created new TranslatableEntityFieldTranslation for provided dto - {}", dto) }
    }

    //endregion

    //region updateValue

    @Transactional
    override fun updateValue(dto: TranslatableEntityFieldTranslationDto): TranslatableEntityFieldTranslation = dto
            .also { logger.trace("Updating TranslatableEntityFieldTranslation for provided dto - {} ", it) }
            .let { getByFieldAndLanguage(dto.key, dto.type, dto.uuid, dto.label, dto.lang) }
            .apply { value = dto.value }
            .let { translatableEntityFieldTranslationRepository.save(it) }
            .also { logger.debug("Successfully updated TranslatableEntityFieldTranslation for provided dto - {}", dto) }

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityFieldTranslationServiceImpl::class.java)
    }
}