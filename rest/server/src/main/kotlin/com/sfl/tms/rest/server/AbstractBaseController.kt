package com.sfl.tms.rest.server

import com.sfl.tms.rest.common.model.AbstractApiModel
import com.sfl.tms.rest.common.model.ResultModel
import com.sfl.tms.rest.common.model.error.ErrorType
import com.sfl.tms.rest.common.model.response.AbstractApiResponseModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * User: Vazgen Danielyan
 * Date: 7/16/18
 * Time: 9:52 PM
 */
abstract class AbstractBaseController {
    companion object {
        @JvmStatic
        fun ok(response: AbstractApiResponseModel): ResponseEntity<ResultModel<out AbstractApiModel>> = ResponseEntity(ResultModel(response), HttpStatus.OK)

        @JvmStatic
        fun created(response: AbstractApiResponseModel): ResponseEntity<ResultModel<out AbstractApiModel>> = ResponseEntity(ResultModel(response), HttpStatus.CREATED)

        @JvmStatic
        fun badRequest(errorType: ErrorType): ResponseEntity<ResultModel<out AbstractApiModel>> = ResponseEntity(ResultModel(listOf(errorType)), HttpStatus.BAD_REQUEST)

        @JvmStatic
        fun notFound(errorType: ErrorType): ResponseEntity<ResultModel<out AbstractApiModel>> = ResponseEntity(ResultModel(listOf(errorType)), HttpStatus.NOT_FOUND)

        @JvmStatic
        fun internal(errorType: ErrorType): ResponseEntity<ResultModel<out AbstractApiModel>> = ResponseEntity(ResultModel(listOf(errorType)), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}