package com.sfl.tms.core.service.translatable.field.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityFieldExistsForTranslatableEntityException(key: String, uuid: String, label: String) :
    RuntimeException("TranslatableEntityField already exists by '$key' key for TranslatableEntity by '$uuid' uuid and '$label' label.")