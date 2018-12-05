package com.sfl.qup.tms.service.language.impl

import com.sfl.qup.tms.domain.language.Language
import com.sfl.qup.tms.persistence.language.LanguageRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByLangException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:13 PM
 */
@Service
class LanguageServiceImpl : LanguageService {

    //region Injection

    @Autowired
    private lateinit var languageRepository: LanguageRepository

    //endregion

    @Transactional(readOnly = true)
    @Throws(LanguageNotFoundByLangException::class)
    override fun get(lang: String): Language = lang
            .also { logger.trace("Retrieving language for provided lang - {} ", lang) }
            .let {
                languageRepository.findByLang(it).let {
                    if (it == null) {
                        logger.error("Can not find language for lang - {}", lang)
                        throw LanguageNotFoundByLangException(lang)
                    }
                    logger.debug("Retrieved language for provided lang - {} ", lang)
                    it
                }
            }

    @Transactional(readOnly = true)
    override fun find(lang: String): Language? = lang
            .also { logger.trace("Retrieving language for provided lang - {} ", lang) }
            .let { languageRepository.findByLang(it) }
            .also { logger.debug("Retrieved language for provided lang - {} ", lang) }

    @Transactional
    override fun create(lang: String): Language = lang
            .also { logger.trace("Creating new language for provided lang - {} ", lang) }
            .let {
                languageRepository.findByLang(it).let {
                    if (it == null) {
                        Language()
                                .apply { this.lang = lang }
                                .let { languageRepository.save(it) }
                                .also { logger.debug("Successfully created new Language for provided lang - {}", lang) }
                    } else {
                        logger.debug("Language with {} lang already exists. Skipping creation.", lang)
                        it
                    }
                }
            }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(LanguageServiceImpl::class.java)
    }
}