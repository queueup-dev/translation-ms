package com.sfl.tms.rest.common.model.error

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.model.AbstractApiModel

/**
 * User: Vazgen Danielyan
 * Date: 9/1/15
 * Time: 10:52 PM
 */
@JsonSubTypes.Type(value = TranslationControllerErrorType::class, name = "translationControllerErrorType")
interface ErrorType : AbstractApiModel
