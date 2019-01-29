package com.sfl.tms.core.service.translatable.translation.dto

import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType

/**
 * User: Vazgen Danielyan
 * Date: 1/16/19
 * Time: 3:37 PM
 */
data class TranslatableEntityFieldTranslationDto(val key: String, val type: TranslatableEntityFieldType, val value: String?, val uuid: String, val label: String, val lang: String)