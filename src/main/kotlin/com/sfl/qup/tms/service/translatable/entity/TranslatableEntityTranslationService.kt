package com.sfl.qup.tms.service.translatable.entity

import com.sfl.qup.tms.domain.translatable.TranslatableEntityTranslation
import com.sfl.qup.tms.service.translatable.entity.dto.TranslatableEntityTranslationDto

/**
 * User: Vazgen Danielyan
 * Date: 12/9/18
 * Time: 4:55 PM
 */
interface TranslatableEntityTranslationService {

    fun create(dto: TranslatableEntityTranslationDto): TranslatableEntityTranslation

}