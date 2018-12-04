package com.sfl.qup.tms.api.config.secutiry

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * User: Vazgen Danielyan
 * Date: 7/16/18
 * Time: 4:21 PM
 */
@Component
@Order(1)
class SecurityFilter : Filter {

    //region Injection

    @Autowired
    private lateinit var handlerMapping: HandlerMapping

    //endregion

    override fun init(filterConfig: FilterConfig) {
        // do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        val handler = handlerMapping.getHandler(httpServletRequest)

        /*if (handler != null) {
            (handler.handler as HandlerMethod).method.declaredAnnotations.forEach {
                if (it is AllowAuthorized) {
                    httpServletRequest.getHeader("Authorization").let {
                        if (StringUtils.isBlank(it)) {
                            httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
                            return
                        } else {
                            try {
                                userService.findByAccessToken(it).let {
                                    if (!it.enabled) {
                                        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
                                        return
                                    }
                                }
                            } catch (e: InvalidAccessTokenException) {
                                httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
                                return
                            }
                        }
                    }
                }
            }
        }*/

        chain.doFilter(request, response)
    }

    override fun destroy() {
        // do nothing
    }
}