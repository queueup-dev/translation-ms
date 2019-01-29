package com.sfl.tms.core.service.translatable.field.dto

import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:09 PM
 */
data class TranslatableEntityFieldDto(val key: String, val type: TranslatableEntityFieldType, val uuid: String, val label: String)