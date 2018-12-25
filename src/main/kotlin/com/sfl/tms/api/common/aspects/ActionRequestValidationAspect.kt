package com.sfl.tms.api.common.aspects

import com.sfl.tms.api.common.model.ResultModel
import com.sfl.tms.api.common.model.error.ErrorType
import com.sfl.tms.api.common.model.request.AbstractApiRequestModel
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

/**
 * User: Vazgen Danielyan
 * Company: SFL LLC
 * Date: 1/25/16
 * Time: 11:21 PM
 */
@Aspect
@Component
class ActionRequestValidationAspect {

    @Around("@annotation(com.sfl.tms.api.common.annotations.ValidateActionRequest)")
    @Throws(Throwable::class)
    fun validateActionRequest(joinPoint: ProceedingJoinPoint): Any {
        val joinPointResult: Any
        try {
            getActionRequest(joinPoint.args).let {
                if (it != null) {
                    ArrayList(it.validateRequiredFields()).let {
                        if (!it.isEmpty()) {
                            return ResponseEntity(ResultModel<ErrorType>(it), HttpStatus.BAD_REQUEST)
                        }
                    }
                }
            }
            joinPointResult = joinPoint.proceed()
        } catch (ex: Exception) {
            LOGGER.error("Failed to proceed join point", ex)
            throw ex
        }

        return joinPointResult
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(ActionRequestValidationAspect::class.java)

        @JvmStatic
        private fun getActionRequest(args: Array<Any>): AbstractApiRequestModel? {
            args.forEach {
                if (it is AbstractApiRequestModel) {
                    return it
                }
            }
            return null
        }
    }
}
