package com.sfl.qup.tms.service.translatable.impl

import com.sfl.qup.tms.persistence.translatable.TranslatableEntityTranslationRepository
import com.sfl.qup.tms.service.translatable.TranslatableEntityTranslationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 6:12 PM
 */
@Service
class TranslatableEntityTranslationServiceImpl : TranslatableEntityTranslationService {

    //region Injection

    @Autowired
    private lateinit var translatableEntityTranslationRepository: TranslatableEntityTranslationRepository

    //endregion

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(TranslatableEntityTranslationServiceImpl::class.java)
    }

}