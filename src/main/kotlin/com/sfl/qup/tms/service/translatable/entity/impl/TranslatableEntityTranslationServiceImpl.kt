package com.sfl.qup.tms.service.translatable.entity.impl

import com.sfl.qup.tms.domain.translatable.TranslatableEntityTranslation
import com.sfl.qup.tms.persistence.translatable.TranslatableEntityTranslationRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.qup.tms.service.translatable.entity.TranslatableEntityTranslationService
import com.sfl.qup.tms.service.translatable.entity.dto.TranslatableEntityTranslationDto
import com.sfl.qup.tms.service.translatable.entity.exception.TranslatableEntityTranslationExistException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: Vazgen Danielyan
 * Date: 12/9/18
 * Time: 4:56 PM
 */
@Service
class TranslatableEntityTranslationServiceImpl : TranslatableEntityTranslationService {

    //region Injection

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var languageService: LanguageService

    @Autowired
    private lateinit var translatableEntityTranslationRepository: TranslatableEntityTranslationRepository

    //endregion

    @Throws(TranslatableEntityTranslationExistException::class)
    @Transactional
    override fun create(dto: TranslatableEntityTranslationDto): TranslatableEntityTranslation {

        logger.trace("Creating new TranslatableEntityTranslation for provided dto - {} ", dto)

        val searchedLanguage = languageService.getByLang(dto.lang)

        val translatableEntity = translatableEntityService.getByUuid(dto.entityUuid)

        val translatableEntityTranslation = translatableEntityTranslationRepository.findByLanguage_IdAndEntity_Id(searchedLanguage.id, translatableEntity.id)

        if (translatableEntityTranslation != null) {
            logger.error("Unable to create new TranslatableEntityTranslation for provided dto - {}. Already exists.", dto)
            throw TranslatableEntityTranslationExistException(dto.lang, dto.entityUuid)
        }

        // Create new translation
        return TranslatableEntityTranslation()
                .apply { text = dto.text }
                .apply { entity = translatableEntity }
                .apply { language = searchedLanguage }
                .let { translatableEntityTranslationRepository.save(it) }
                .also { logger.debug("Successfully created new TranslatableEntityTranslation for provided dto - {}", dto) }
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityTranslationServiceImpl::class.java)
    }

}