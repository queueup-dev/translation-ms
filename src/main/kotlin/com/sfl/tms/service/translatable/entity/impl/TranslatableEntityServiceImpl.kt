package com.sfl.tms.service.translatable.entity.impl

import com.sfl.tms.domain.translatable.TranslatableEntity
import com.sfl.tms.persistence.translatable.TranslatableEntityRepository
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityExistsByUuidException
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidException
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

    @Transactional(readOnly = true)
    override fun findByUuid(uuid: String): TranslatableEntity? = uuid
            .also { logger.trace("Retrieving TranslatableEntity for provided uuid - {} ", uuid) }
            .let { translatableEntityRepository.findByUuid(it) }
            .also { logger.debug("Retrieved TranslatableEntity for provided uuid - {} ", uuid) }

    @Throws(TranslatableEntityNotFoundByUuidException::class)
    @Transactional(readOnly = true)
    override fun getByUuid(uuid: String): TranslatableEntity = uuid
            .also { logger.trace("Retrieving TranslatableEntity for provided uuid - {} ", uuid) }
            .let {
                findByUuid(uuid).let {
                    if (it == null) {
                        logger.error("Can't find TranslatableEntity for uuid - {}", uuid)
                        throw TranslatableEntityNotFoundByUuidException(uuid)
                    }
                    logger.debug("Retrieved TranslatableEntity for provided uuid - {} ", uuid)
                    it
                }
            }

    @Throws(TranslatableEntityExistsByUuidException::class)
    @Transactional
    override fun create(dto: TranslatableEntityDto): TranslatableEntity = dto
            .also { logger.trace("Creating new TranslatableEntity for provided dto - {} ", dto) }
            .let {
                findByUuid(dto.uuid).let {
                    if (it == null) {
                        TranslatableEntity()
                                .apply { uuid = dto.uuid }
                                .apply { name = dto.name }
                                .let { translatableEntityRepository.save(it) }
                                .also { logger.debug("Successfully created new TranslatableEntity for provided dto - {}", dto) }
                    } else {
                        logger.error("Unable to create new TranslatableEntity for provided dto - {}. Already exists.", dto)
                        throw TranslatableEntityExistsByUuidException(dto.uuid)
                    }
                }
            }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityServiceImpl::class.java)
    }
}