package com.sfl.qup.tms.service.translatable.dto.entity

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:09 PM
 */
data class TranslatableEntityDto(val uuid: String, val name: String, val translations: List<TranslatableEntityTranslationDto> = emptyList())