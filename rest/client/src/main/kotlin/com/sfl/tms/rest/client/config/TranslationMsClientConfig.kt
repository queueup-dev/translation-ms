package com.sfl.tms.rest.client.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:27 PM
 */
@ComponentScan(basePackages = ["com.sfl.tms.rest.client"])
@Configuration
@PropertySource(value = ["classpath:translation-ms-client.properties", "file:\${user.home}/translation-ms-client.properties"], ignoreResourceNotFound = true)
@Component
class TranslationMsClientConfig {

    //region Properties

    @Value("\${language.api}")
    internal lateinit var languageApi: String

    @Value("\${translation.api}")
    internal lateinit var translationApi: String

    //endregion
}