package com.sfl.qup.tms.service.translatablestatic.impl

import com.sfl.qup.tms.domain.translatablestastic.TranslatableStatic
import com.sfl.qup.tms.persistence.translatablestastics.TranslatableStaticRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.translatablestatic.TranslatableStaticService
import com.sfl.qup.tms.service.translatablestatic.dto.TranslatableStaticDto
import com.sfl.qup.tms.service.translatablestatic.exception.TranslatableStaticExistException
import com.sfl.qup.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndLanguageLangException
import com.sfl.qup.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: Vazgen Danielyan
 * Date: 12/11/18
 * Time: 1:51 AM
 */
@Service
class TranslatableStaticServiceImpl : TranslatableStaticService {

    //region Injection

    @Autowired
    private lateinit var translatableStaticRepository: TranslatableStaticRepository

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    @Transactional(readOnly = true)
    override fun findByKeyAndLanguageLang(key: String, lang: String): TranslatableStatic? = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, lang - {}", key, lang) }
            .let { translatableStaticRepository.findByKeyAndLanguage_Lang(it, lang) }
            .also { logger.debug("Retrieved TranslatableStatic for provided key - {}, lang - {}", key, lang) }

    @Throws(TranslatableStaticNotFoundByKeyAndLanguageLangException::class)
    @Transactional(readOnly = true)
    override fun getByKeyAndLanguageLang(key: String, lang: String): TranslatableStatic = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, lang - {}", key, lang) }
            .let {
                findByKeyAndLanguageLang(it, lang).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableStatic for key - {}, lang - {}", key, lang)
                        throw TranslatableStaticNotFoundByKeyAndLanguageLangException(key, lang)
                    }
                    logger.debug("Retrieved TranslatableStatic for provided key - {}, lang - {}", key, lang)
                    it
                }
            }

    @Throws(TranslatableStaticNotFoundByKeyException::class)
    @Transactional(readOnly = true)
    override fun getByKey(key: String): List<TranslatableStatic> = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}", it) }
            .let {
                translatableStaticRepository.findByKey(it).let {
                    if (it.isEmpty()) {
                        logger.error("Can't find TranslatableStatic for key - {}", key)
                        throw TranslatableStaticNotFoundByKeyException(key)
                    }
                    logger.debug("Retrieved TranslatableStatic for provided key - {}", key)
                    it
                }
            }

    @Throws(TranslatableStaticExistException::class)
    @Transactional
    override fun create(dto: TranslatableStaticDto): TranslatableStatic {
        logger.trace("Creating new TranslatableStatic for provided dto - {} ", dto)

        val searchedLanguage = languageService.getByLang(dto.lang)

        val translatableStatics = translatableStaticRepository.findByKeyAndLanguage_Id(dto.key, searchedLanguage.id)

        if (translatableStatics != null) {
            logger.error("Unable to create new TranslatableStatic for provided dto - {}. Already exists.", dto)
            throw TranslatableStaticExistException(dto.key, dto.lang)
        }

        // Create new translation
        return TranslatableStatic()
                .apply { key = dto.key }
                .apply { value = dto.value }
                .apply { language = searchedLanguage }
                .let { translatableStaticRepository.save(it) }
                .also { logger.debug("Successfully created new TranslatableStatic for provided dto - {}", dto) }
    }

    @Transactional
    override fun update(dto: TranslatableStaticDto): TranslatableStatic = dto
            .also { logger.trace("Updating TranslatableStatic for provided dto - {} ", it) }
            .let { getByKeyAndLanguageLang(dto.key, dto.lang) }
            .apply { value = dto.value }
            .let { translatableStaticRepository.save(it) }
            .also { logger.debug("Successfully updated TranslatableStatic for provided dto - {}", dto) }

    @Transactional(readOnly = true)
    override fun search(term: String?, page: Int?): List<TranslatableStatic> = PageRequest.of(page ?: 0, 15)
            .also { logger.trace("Retrieving TranslatableStatic for provided search term - {}, page id - {}", term, page) }
            .let {
                if (term == null) {
                    translatableStaticRepository.findByOrderByKeyAsc(it)
                } else {
                    translatableStaticRepository.findByKeyLikeOrderByKeyAsc("$term%", it)
                }
            }
            .also { logger.debug("Retrieved TranslatableStatic for provided term - {}, page - {}", term, page) }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableStaticServiceImpl::class.java)
    }

}