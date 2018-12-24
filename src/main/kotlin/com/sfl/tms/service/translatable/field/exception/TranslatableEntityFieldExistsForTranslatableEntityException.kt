package com.sfl.tms.service.translatable.field.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityFieldExistsForTranslatableEntityException(fieldName: String, entityUuid: String) :
        RuntimeException("TranslatableEntityField already exists by $fieldName fieldName for TranslatableEntity by $entityUuid entityUuid.")