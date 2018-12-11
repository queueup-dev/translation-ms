package com.sfl.qup.tms.service.translatablestatics.impl

import com.sfl.qup.tms.domain.translatablestastics.TranslatableStatic
import com.sfl.qup.tms.persistence.translatablestastics.TranslatableStaticsRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByIdException
import com.sfl.qup.tms.service.translatablestatics.TranslatableStaticsService
import com.sfl.qup.tms.service.translatablestatics.dto.TranslatableStaticDto
import com.sfl.qup.tms.service.translatablestatics.exception.TranslatableStaticNotFoundByKeyAndLanguageIdException
import com.sfl.qup.tms.service.translatablestatics.exception.TranslatableStaticsExistException
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
class TranslatableStaticsServiceImpl : TranslatableStaticsService {

    //region Injection

    @Autowired
    private lateinit var translatableStaticsRepository: TranslatableStaticsRepository

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    @Transactional(readOnly = true)
    override fun findByKeyAndLanguageId(key: String, languageId: Long): TranslatableStatic? = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, language id- {}", key, languageId) }
            .let { translatableStaticsRepository.findByKeyAndLanguage_Id(it, languageId) }
            .also { logger.debug("Retrieved TranslatableStatic for provided key - {}, language id- {}", key, languageId) }

    @Throws(TranslatableStaticNotFoundByKeyAndLanguageIdException::class)
    @Transactional(readOnly = true)
    override fun getByKeyAndLanguageId(key: String, languageId: Long): TranslatableStatic = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, language id - {}", key, languageId) }
            .let {
                findByKeyAndLanguageId(it, languageId).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableStatic for key - {}, language id- {}", key, languageId)
                        throw TranslatableStaticNotFoundByKeyAndLanguageIdException(key, languageId)
                    }
                    logger.debug("Retrieved TranslatableStatic for provided key - {}, language id - {}", key, languageId)
                    it
                }
            }

    @Throws(TranslatableStaticsExistException::class, LanguageNotFoundByIdException::class)
    @Transactional(rollbackFor = [Exception::class])
    override fun create(dto: TranslatableStaticDto): TranslatableStatic {
        logger.trace("Creating new TranslatableStatic for provided dto - {} ", dto)

        val searchedLanguage = languageService.get(dto.languageId)

        val translatableStatics = translatableStaticsRepository.findByKeyAndLanguage_Id(dto.key, searchedLanguage.id)

        if (translatableStatics != null) {
            logger.error("Unable to create new TranslatableStatic for provided dto - {}. Already exists.", dto)
            throw TranslatableStaticsExistException(dto.key, searchedLanguage.lang)
        }

        // Create new translation
        return TranslatableStatic()
                .apply { key = dto.key }
                .apply { value = dto.value }
                .apply { language = searchedLanguage }
                .let { translatableStaticsRepository.save(it) }
                .also { logger.debug("Successfully created new TranslatableStatic for provided dto - {}", dto) }
    }

    @Transactional(readOnly = true)
    override fun search(term: String?, page: Int?): List<TranslatableStatic> = PageRequest.of(page ?: 0, 15)
            .also { logger.trace("Retrieving TranslatableStatic for provided search term - {}, page id - {}", term, page) }
            .let {
                if (term == null) {
                    translatableStaticsRepository.findByOrderByKeyAsc(it)
                } else {
                    translatableStaticsRepository.findByKeyLikeOrderByKeyAsc("$term%", it)
                }
            }
            .also { logger.debug("Retrieved TranslatableStatic for provided term - {}, page - {}", term, page) }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableStaticsServiceImpl::class.java)
    }

}