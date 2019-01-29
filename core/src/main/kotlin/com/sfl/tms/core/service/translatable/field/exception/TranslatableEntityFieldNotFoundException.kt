package com.sfl.tms.core.service.translatable.field.exception

import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityFieldNotFoundException(msg: String) : RuntimeException(msg) {
    constructor(key: String, type: TranslatableEntityFieldType, uuid: String, label: String) : this("TranslatableEntityField not found by '$key' key, '$type' type, '$uuid' uuid and '$label' label.")
}