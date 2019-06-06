package com.sfl.tms.core.service.translatable.entity.impl

import com.sfl.tms.core.domain.translatable.TranslatableEntity
import com.sfl.tms.core.persistence.translatable.TranslatableEntityRepository
import com.sfl.tms.core.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.core.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityExistsException
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityNotFoundException
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
class TranslatableEntityServiceImpl : TranslatableEntityService {

    //region Injection

    @Autowired
    private lateinit var translatableEntityRepository: TranslatableEntityRepository

    //endregion

    //region findAll

    @Transactional(readOnly = true)
    override fun findAll(): List<TranslatableEntity> {
        logger.trace("Retrieving all TranslatableEntities")
        return translatableEntityRepository.findAll().also { logger.debug("Retrieved all TranslatableEntities") }
    }

    //endregion

    //region findByUuidAndLabel

    @Transactional(readOnly = true)
    override fun findByUuidAndLabel(uuid: String, label: String): TranslatableEntity? = uuid
        .also { logger.trace("Retrieving TranslatableEntity for provided uuid - {} and label - {}", uuid, label) }
        .let { translatableEntityRepository.findByUuidAndLabel(it, label) }
        .also { logger.debug("Retrieved TranslatableEntity for provided uuid - {} and label - {}", uuid, label) }

    @Transactional(readOnly = true)
    override fun findByUuidAndLabels(uuid: String, labels: List<String>): List<TranslatableEntity> = uuid
            .also { logger.trace("Retrieving TranslatableEntity for provided uuid - {} and labels - {}", uuid, labels) }
            .let { translatableEntityRepository.findByUuidAndLabelIn(it, labels) }
            .also { logger.debug("Retrieved TranslatableEntity for provided uuid - {} and label - {}", uuid, labels) }
    //endregion

    //region findByUuid

    @Transactional(readOnly = true)
    override fun findByUuid(uuid: String): List<TranslatableEntity> = uuid
            .also { logger.trace("Retrieving TranslatableEntity for provided uuid - {}", uuid) }
            .let { translatableEntityRepository.findByUuid(it) }
            .also { logger.debug("Retrieved TranslatableEntity for provided uuid - {} and label - {}", uuid) }

    //endregion

    //region getByUuidAndLabel

    @Throws(TranslatableEntityNotFoundException::class)
    @Transactional(readOnly = true)
    override fun getByUuidAndLabel(uuid: String, label: String): TranslatableEntity = uuid
        .also { logger.trace("Retrieving TranslatableEntity for provided uuid - {} and label - {}", uuid, label) }
        .let {
            findByUuidAndLabel(uuid, label).let {
                if (it == null) {
                    logger.error("Can't find TranslatableEntity for uuid - {} and label - {}", uuid, label)
                    throw TranslatableEntityNotFoundException(uuid, label)
                }
                logger.debug("Retrieved TranslatableEntity for provided uuid - {} and label - {}", uuid, label)
                it
            }
        }

    //endregion

    //region create

    @Throws(TranslatableEntityExistsException::class)
    @Transactional
    override fun create(dto: TranslatableEntityDto): TranslatableEntity = dto
        .also { logger.trace("Creating new TranslatableEntity for provided dto - {} ", dto) }
        .let {
            findByUuidAndLabel(dto.uuid, dto.label).let {
                if (it == null) {
                    TranslatableEntity()
                        .apply { uuid = dto.uuid }
                        .apply { label = dto.label }
                        .apply { name = dto.name }
                        .let { translatableEntityRepository.save(it) }
                        .also { logger.debug("Successfully created new TranslatableEntity for provided dto - {}", dto) }
                } else {
                    logger.error("Unable to create new TranslatableEntity for provided dto - {}. Already exists.", dto)
                    throw TranslatableEntityExistsException(dto.uuid, dto.label)
                }
            }
        }

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityServiceImpl::class.java)
    }
}