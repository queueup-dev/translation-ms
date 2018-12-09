package com.sfl.qup.tms.service.translatable.field.exception

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:11 PM
 */
class TranslatableEntityFieldNotFoundByNameAndEntityUuidException(name: String, entityUuid: String) : RuntimeException("TranslatableEntityField not found by $name name and $entityUuid uuid.")