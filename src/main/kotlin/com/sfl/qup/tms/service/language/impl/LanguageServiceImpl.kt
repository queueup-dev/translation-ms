package com.sfl.qup.tms.service.language.impl

import com.sfl.qup.tms.domain.language.Language
import com.sfl.qup.tms.persistence.language.LanguageRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByLangException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

    override fun create(lang: String): Language {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    override fun find(lang: String): Language? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(LanguageServiceImpl::class.java)
    }
}