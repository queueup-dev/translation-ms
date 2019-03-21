package com.sfl.tms.core.service.translatable.field.impl

import com.sfl.tms.core.domain.translatable.TranslatableEntityField
import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.core.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.tms.core.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityNotFoundException
import com.sfl.tms.core.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.core.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
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

    //region findByKeyAndTypeAndEntity

    @Transactional(readOnly = true)
    override fun findByKeyAndTypeAndEntity(key: String, type: TranslatableEntityFieldType, uuid: String, label: String): TranslatableEntityField? = key
        .also { logger.trace("Retrieving TranslatableEntityField by provided key - {}, type - {} and TranslatableEntity uuid - {} and label - {}.", key, type, uuid, label) }
        .let { translatableEntityFieldRepository.findByKeyAndTypeAndEntity(it, type, translatableEntityService.getByUuidAndLabel(uuid, label)) }
        .also { logger.debug("Retrieved TranslatableEntityField for provided key - {} and TranslatableEntity uuid - {} and label - {}.", key, uuid, label) }

    //endregion

    //region getByKeyAndTypeAndEntity

    @Throws(TranslatableEntityFieldNotFoundException::class)
    @Transactional(readOnly = true)
    override fun getByKeyAndTypeAndEntity(key: String, type: TranslatableEntityFieldType, uuid: String, label: String): TranslatableEntityField = key
        .also { logger.trace("Retrieving TranslatableEntityField for provided key - {}, type - {}, uuid - {} and label - {}.", key, type, uuid, label) }
        .let {
            findByKeyAndTypeAndEntity(it, type, uuid, label).let {
                if (it == null) {
                    logger.error("Can't find TranslatableEntityField for key - {}, type - {}, uuid - {} and label - {}.", key, type, uuid, label)
                    throw TranslatableEntityFieldNotFoundException(key, type, uuid, label)
                }
                logger.debug("Retrieved TranslatableEntityField for provided key - {}, type - {}, uuid - {} and label - {}.", key, type, uuid, label)
                it
            }
        }

    //endregion

    //region create

    @Throws(TranslatableEntityNotFoundException::class, TranslatableEntityFieldExistsForTranslatableEntityException::class)
    @Transactional
    override fun create(dto: TranslatableEntityFieldDto): TranslatableEntityField = dto
        .also { logger.trace("Creating new TranslatableEntityField for provided dto - {} ", dto) }
        .let {
            val translatableEntity = translatableEntityService.getByUuidAndLabel(dto.uuid, dto.label)

            findByKeyAndTypeAndEntity(dto.key, dto.type, dto.uuid, dto.label).let {
                if (it == null) {
                    TranslatableEntityField()
                        .apply { key = dto.key }
                        .apply { type = dto.type }
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