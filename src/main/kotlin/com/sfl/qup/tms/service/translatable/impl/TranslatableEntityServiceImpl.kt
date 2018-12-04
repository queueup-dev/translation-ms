package com.sfl.qup.tms.service.translatable.impl

import com.sfl.qup.tms.persistence.translatable.TranslatableEntityRepository
import com.sfl.qup.tms.service.translatable.TranslatableEntityService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityServiceImpl::class.java)
    }

}