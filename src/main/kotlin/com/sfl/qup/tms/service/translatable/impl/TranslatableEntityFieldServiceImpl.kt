package com.sfl.qup.tms.service.translatable.impl

import com.sfl.qup.tms.domain.translatable.TranslatableEntityField
import com.sfl.qup.tms.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.qup.tms.service.translatable.TranslatableEntityFieldService
import com.sfl.qup.tms.service.translatable.TranslatableEntityService
import com.sfl.qup.tms.service.translatable.dto.field.TranslatableEntityFieldDto
import com.sfl.qup.tms.service.translatable.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.qup.tms.service.translatable.exception.TranslatableEntityNotFoundByUuidException
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

    @Throws(TranslatableEntityNotFoundByUuidException::class, TranslatableEntityFieldExistsForTranslatableEntityException::class)
    @Transactional
    override fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField = dto
            .also { logger.trace("Creating new TranslatableEntityField for provided dto - {} ", dto) }
            .let {
                val translatableEntity = translatableEntityService.findByUuid(dto.uuid)
                if (translatableEntity == null) {
                    logger.error("Unable to create new TranslatableEntityField for provided dto - {}. TranslatableEntity not found by {} uuid.", dto, dto.uuid)
                    throw TranslatableEntityNotFoundByUuidException(dto.uuid)
                } else {
                    find(dto.name, dto.uuid).let {
                        if (it == null) {
                            TranslatableEntityField()
                                    .apply { name = dto.name }
                                    .apply { entity = translatableEntity }
                                    .let { translatableEntityFieldRepository.save(it) }
                                    .also { logger.debug("Successfully created new TranslatableEntityField for provided dto - {}", dto) }
                        } else {
                            logger.error("Unable to create new TranslatableEntityField for provided dto - {}. TranslatableEntityField exist by {} name", dto, dto.name)
                            throw TranslatableEntityFieldExistsForTranslatableEntityException(dto.name, dto.uuid)
                        }
                    }
                }
            }

    @Transactional(readOnly = true)
    override fun find(name: String, uuid: String): TranslatableEntityField? = name
            .also { logger.trace("Retrieving TranslatableEntityField by provided name - {} and TranslatableEntity {} uuid.", name, uuid) }
            .let { translatableEntityFieldRepository.findByNameAndEntity_Uuid(name, uuid) }
            .also { logger.debug("Retrieved TranslatableEntityField for provided name - {} and TranslatableEntity {} uuid.", name, uuid) }


    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityFieldServiceImpl::class.java)
    }

}