package com.sfl.tms.rest.client

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.sfl.tms.rest.client.translation.TranslationApiClient
import com.sfl.tms.rest.common.communicator.translation.error.TranslationControllerErrorType
import com.sfl.tms.rest.common.communicator.translation.request.entity.TranslatableEntityCreateRequestModel
import com.sfl.tms.rest.common.model.error.ErrorType
import feign.Feign
import feign.Logger
import feign.Request
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.context.annotation.Bean
import java.lang.reflect.Type

/**
 * User: Vazgen Danielyan
 * Date: 1/27/19
 * Time: 11:55 PM
 */
@EnableFeignClients
@SpringBootApplication
class ClientApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ClientApplication::class.java, *args)
        }
    }

    @Bean
    fun init(): CommandLineRunner = CommandLineRunner {
        Feign
                .builder()
                .logLevel(Logger.Level.FULL)
                .contract(SpringMvcContract())
                .client(OkHttpClient())
                .encoder(GsonEncoder(GsonBuilder().registerTypeHierarchyAdapter(ErrorType::class.java, JsonDeserializer<ErrorType> { json, _, _ -> TranslationControllerErrorType.valueOf((json as JsonPrimitive).asString.replace("\"", "")) }).create()))
                .decoder(GsonDecoder(GsonBuilder().registerTypeHierarchyAdapter(ErrorType::class.java, JsonDeserializer<ErrorType> { json, _, _ -> TranslationControllerErrorType.valueOf((json as JsonPrimitive).asString.replace("\"", "")) }).create()))
                .options(Request.Options(100000, 100000))
                .logger(Slf4jLogger(TranslationApiClient::class.java))
                .target(TranslationApiClient::class.java, "http://localhost:8080/")
                .let {
                    it.createTranslatableEntity(TranslatableEntityCreateRequestModel("ddddbcddda", "bdcdddddda", "baddddddcd")).let {
                        if (it.success) {
                            println(it.result)
                        } else {
                            println(it.errors)
                        }
                    }

                }

    }
}