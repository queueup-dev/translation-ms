package com.sfl.qup.tms.service.translatable.field.impl

import com.sfl.qup.tms.domain.translatable.TranslatableEntityField
import com.sfl.qup.tms.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.qup.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.qup.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.qup.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.qup.tms.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.qup.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundByNameAndEntityUuidException
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

    @Transactional(readOnly = true)
    override fun findByNameAndEntityUuid(name: String, entityUuid: String): TranslatableEntityField? = name
            .also { logger.trace("Retrieving TranslatableEntityField by provided name - {} and TranslatableEntity {} entityUuid.", name, entityUuid) }
            .let { translatableEntityFieldRepository.findByNameAndEntity_Uuid(name, entityUuid) }
            .also { logger.debug("Retrieved TranslatableEntityField for provided name - {} and TranslatableEntity {} entityUuid.", name, entityUuid) }

    @Throws(TranslatableEntityFieldNotFoundByNameAndEntityUuidException::class)
    @Transactional(readOnly = true)
    override fun getByNameAndEntityUuid(name: String, entityUuid: String): TranslatableEntityField = name
            .also { logger.trace("Retrieving TranslatableEntityField for provided name - {} and entityUuid - {}.", name, entityUuid) }
            .let {
                findByNameAndEntityUuid(name, entityUuid).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableEntityField for name - {} and entityUuid - {}.", name, entityUuid)
                        throw TranslatableEntityFieldNotFoundByNameAndEntityUuidException(name, entityUuid)
                    }
                    logger.debug("Retrieved TranslatableEntityField for provided name - {} and entityUuid - {}.", name, entityUuid)
                    it
                }
            }

    @Throws(TranslatableEntityFieldExistsForTranslatableEntityException::class)
    @Transactional
    override fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField = dto
            .also { logger.trace("Creating new TranslatableEntityField for provided dto - {} ", dto) }
            .let {
                val translatableEntity = translatableEntityService.getByUuid(dto.entityUuid)

                findByNameAndEntityUuid(dto.name, dto.entityUuid).let {
                    if (it == null) {
                        TranslatableEntityField()
                                .apply { name = dto.name }
                                .apply { entity = translatableEntity }
                                .let { translatableEntityFieldRepository.save(it) }
                                .also { logger.debug("Successfully created new TranslatableEntityField for provided dto - {}", dto) }
                    } else {
                        logger.error("Unable to create new TranslatableEntityField for provided dto - {}. Already exists.", dto)
                        throw TranslatableEntityFieldExistsForTranslatableEntityException(dto.name, dto.entityUuid)
                    }
                }
            }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityFieldServiceImpl::class.java)
    }

}