package com.sfl.qup.tms.service.translatablestatics

import com.sfl.qup.tms.domain.translatablestastics.TranslatableStatic
import com.sfl.qup.tms.service.translatablestatics.dto.TranslatableStaticDto

/**
 * User: Vazgen Danielyan
 * Date: 12/11/18
 * Time: 1:51 AM
 */
interface TranslatableStaticsService {

    fun findByKeyAndLanguageId(key: String, languageId: Long): TranslatableStatic?

    fun getByKeyAndLanguageId(key: String, languageId: Long): TranslatableStatic

    fun create(dto: TranslatableStaticDto): TranslatableStatic

    fun search(term: String?, page: Int?): List<TranslatableStatic>

}