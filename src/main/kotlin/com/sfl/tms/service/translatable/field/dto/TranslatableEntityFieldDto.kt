package com.sfl.tms.service.translatable.field.dto

import com.sfl.tms.domain.translatable.TranslatableEntityFieldType

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:09 PM
 */
data class TranslatableEntityFieldDto(val key: String, val type: TranslatableEntityFieldType, val uuid: String, val label: String)