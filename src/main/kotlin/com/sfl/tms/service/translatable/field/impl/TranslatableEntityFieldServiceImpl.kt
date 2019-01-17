package com.sfl.tms.service.translatable.field.impl

import com.sfl.tms.domain.translatable.TranslatableEntityField
import com.sfl.tms.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidAndLabelException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:12 PM
 */
@Service
class TranslatableEntityFieldServiceImpl : TranslatableEntityFieldService {

    //region Injection

    @Autowired
    private lateinit var translatableEntityFieldRepository: TranslatableEntityFieldRepository

    @Autowired
    private lateinit var translatableEntityService: TranslatableEntityService

    //endregion

    //region findByKeyAndEntity

    @Transactional(readOnly = true)
    override fun findByKeyAndEntity(key: String, uuid: String, label: String): TranslatableEntityField? = key
            .also { logger.trace("Retrieving TranslatableEntityField by provided key - {} and TranslatableEntity uuid - {} and label - {}.", key, uuid, label) }
            .let { translatableEntityFieldRepository.findByKeyAndEntity(it, translatableEntityService.getByUuidAndLabel(uuid, label)) }
            .also { logger.debug("Retrieved TranslatableEntityField for provided key - {} and TranslatableEntity uuid - {} and label - {}.", key, uuid, label) }

    //endregion

    //region getByKeyAndEntity

    @Throws(TranslatableEntityFieldNotFoundException::class)
    @Transactional(readOnly = true)
    override fun getByKeyAndEntity(key: String, uuid: String, label: String): TranslatableEntityField = key
            .also { logger.trace("Retrieving TranslatableEntityField for provided key - {}, uuid - {} and label - {}.", key, uuid, label) }
            .let {
                findByKeyAndEntity(it, uuid, label).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableEntityField for key - {}, uuid - {} and label - {}.", key, uuid, label)
                        throw TranslatableEntityFieldNotFoundException(key, uuid, label)
                    }
                    logger.debug("Retrieved TranslatableEntityField for provided key - {}, uuid - {} and label - {}.", key, uuid, label)
                    it
                }
            }

    //endregion

    //region create

    @Throws(TranslatableEntityNotFoundByUuidAndLabelException::class, TranslatableEntityFieldExistsForTranslatableEntityException::class)
    @Transactional
    override fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField = dto
            .also { logger.trace("Creating new TranslatableEntityField for provided dto - {} ", dto) }
            .let {
                val translatableEntity = translatableEntityService.getByUuidAndLabel(dto.uuid, dto.label)

                findByKeyAndEntity(dto.key, dto.uuid, dto.label).let {
                    if (it == null) {
                        TranslatableEntityField()
                                .apply { key = dto.key }
                                .apply { entity = translatableEntity }
                                .let { translatableEntityFieldRepository.save(it) }
                                .also { logger.debug("Successfully created new TranslatableEntityField for provided dto - {}", dto) }
                    } else {
                        logger.error("Unable to create new TranslatableEntityField for provided dto - {}. Already exists.", dto)
                        throw TranslatableEntityFieldExistsForTranslatableEntityException(dto.key, dto.uuid, dto.label)
                    }
                }
            }

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityFieldServiceImpl::class.java)
    }

}