package com.sfl.qup.tms.service.translatable.field.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityFieldExistsForTranslatableEntityException(val name: String, val uuid: String) :
        RuntimeException("TranslatableEntityField already exists by $name name for TranslatableEntity by $uuid uuid.")