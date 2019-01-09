package com.sfl.tms.service.translatablestatic.impl

import com.sfl.tms.TmsApplication
import com.sfl.tms.domain.translatablestastic.TranslatableStatic
import com.sfl.tms.persistence.translatablestastics.TranslatableStaticRepository
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidException
import com.sfl.tms.service.translatablestatic.TranslatableStaticService
import com.sfl.tms.service.translatablestatic.dto.TranslatableStaticDto
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticExistException
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndEntityUuidException
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
    private lateinit var translatableEntityService: TranslatableEntityService

    @Autowired
    private lateinit var translatableStaticRepository: TranslatableStaticRepository

    @Autowired
    private lateinit var languageService: LanguageService

    //endregion

    //region findByKeyAndEntityUuidAndLanguageLang

    @Transactional(readOnly = true)
    override fun findByKeyAndEntityUuidAndLanguageLang(key: String, entityUuid: String, lang: String): TranslatableStatic? = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, entity uuid - {} and lang - {}", key, entityUuid, lang) }
            .let { translatableStaticRepository.findByKeyAndEntity_UuidAndLanguage_Lang(it, entityUuid, lang) }
            .also { logger.debug("Retrieved TranslatableStatic for provided key - {}, entity uuid - {} and lang - {}", key, entityUuid, lang) }

    //endregion

    //region getByKeyAndEntityUuid

    @Throws(TranslatableStaticNotFoundByKeyAndEntityUuidException::class, TranslatableEntityNotFoundByUuidException::class)
    @Transactional(readOnly = true)
    override fun getByKeyAndEntityUuid(key: String, uuid: String): List<TranslatableStatic> = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {} and entity uuid - {}", it, uuid) }
            .let {

                val entity = translatableEntityService.getByUuid(uuid)

                translatableStaticRepository.findByKeyAndEntity_Id(it, entity.id).let {
                    if (it.isEmpty()) {
                        logger.error("Can't find TranslatableStatic for key - {} and entity uuid - {}", key, uuid)
                        throw TranslatableStaticNotFoundByKeyAndEntityUuidException(key, uuid)
                    }
                    logger.debug("Retrieved TranslatableStatic for provided key - {} and entity uuid - {}", key, uuid)
                    it
                }
            }

    //endregion

    //region getByKeyAndEntityUuidAndLanguageLang

    @Throws(TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException::class)
    @Transactional(readOnly = true)
    override fun getByKeyAndEntityUuidAndLanguageLang(key: String, uuid: String, lang: String): TranslatableStatic = key
            .also { logger.trace("Retrieving TranslatableStatic for provided key - {}, entity uuid - {} and lang - {}", it, uuid, lang) }
            .let {
                translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Lang(key = it, entityId = translatableEntityService.getByUuid(uuid).id, lang = lang).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableStatic for key - {}, entity uuid - {} and lang - {}", key, uuid, lang)
                        throw TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException(key, uuid, lang)
                    }
                    logger.debug("Retrieved TranslatableStatic for provided key - {}, entity uuid - {} and lang - {}", key, uuid, lang)
                    it
                }
            }

    //endregion

    //region create

    @Throws(TranslatableStaticExistException::class)
    @Transactional
    override fun create(dto: TranslatableStaticDto): TranslatableStatic {
        logger.trace("Creating new TranslatableStatic for provided dto - {} ", dto)

        val language = languageService.getByLang(dto.lang)

        val entity = translatableEntityService.getByUuid(dto.entityUuid)

        val translatableStatics = translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Id(dto.key, entity.id, language.id)

        if (translatableStatics != null) {
            logger.error("Unable to create new TranslatableStatic for provided dto - {}. Already exists.", dto)
            throw TranslatableStaticExistException(dto.key, dto.entityUuid, dto.lang)
        }

        // Create new translation
        return TranslatableStatic()
                .apply { this.key = dto.key }
                .apply { this.value = dto.value }
                .apply { this.entity = entity }
                .apply { this.language = language }
                .let { translatableStaticRepository.save(it) }
                .also { logger.debug("Successfully created new TranslatableStatic for provided dto - {}", dto) }
    }

    //endregion

    //region updateValue

    @Transactional
    override fun updateValue(dto: TranslatableStaticDto): TranslatableStatic = dto
            .also { logger.trace("Updating TranslatableStatic for provided dto - {} ", it) }
            .let { getByKeyAndEntityUuidAndLanguageLang(dto.key, dto.entityUuid, dto.lang) }
            .apply { value = dto.value }
            .let { translatableStaticRepository.save(it) }
            .also { logger.debug("Successfully updated TranslatableStatic for provided dto - {}", dto) }

    //endregion

    //region search

    @Transactional(readOnly = true)
    override fun search(uuid: String, term: String?, lang: String?, page: Int?): List<TranslatableStatic> = PageRequest.of(page ?: 0, 15)
            .also { logger.trace("Retrieving TranslatableStatic for provided uuid - {}, term - {}, language - {} and page id - {}", uuid, term, lang, page) }
            .let {
                if (term == null) {
                    translatableStaticRepository.findByEntityUuidAndLangOrderByKeyAsc(uuid = uuid, lang = lang ?: "", pageable = it)
                } else {
                    translatableStaticRepository.findByEntityUuidAndLangAndKeyLikeOrderByKeyAsc(uuid = uuid, lang = lang ?: "", term = "$term%", pageable = it)
                }
            }
            .also { logger.debug("Retrieved TranslatableStatic for provided uuid - {}, term - {}, language - {} and page id - {}", uuid, term, lang, page) }

    //endregion

    //region copy

    @Transactional
    override fun copy(uuid: String): List<TranslatableStatic> = uuid
            .also { logger.trace("Copying static translations for entity with {} uuid.", it) }
            .let {

                val entity = translatableEntityService.getByUuid(it)

                translatableEntityService.getByUuid(TmsApplication.templateUuid).statics.map {
                    it
                            .copy()
                            .apply { this.entity = entity }
                            .let { translatableStaticRepository.save(it) }
                            .also { logger.debug("Copied static translation with {} key and {} language for entity with {} uuid.", it.key, it.language.lang, it.entity.uuid) }
                }
            }
            .also { logger.trace("Successfully copied static translations for entity with {} uuid.", it) }

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableStaticServiceImpl::class.java)
    }
}