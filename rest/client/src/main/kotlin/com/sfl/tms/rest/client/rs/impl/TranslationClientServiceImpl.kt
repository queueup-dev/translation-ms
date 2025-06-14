package com.sfl.tms.rest.client.rs.impl

import com.sfl.tms.rest.client.rs.TranslationClientService
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.springframework.stereotype.Component
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import jakarta.annotation.PostConstruct
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder

/**
 * User: Vazgen Danielyan
 * Date: 2/11/19
 * Time: 11:22 PM
 */
@Component
open class TranslationClientServiceImpl : TranslationClientService {

    //region Properties

    private lateinit var client: Client

    //endregion

    override fun getClient(): Client = client

    @PostConstruct
    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    fun initMethod() {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(certs: Array<out X509Certificate>?, authType: String?) {
                //disable certificate verification
            }

            override fun checkServerTrusted(certs: Array<out X509Certificate>?, authType: String?) {
                //disable certificate verification
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }), SecureRandom())

        client = ClientBuilder.newBuilder().sslContext(sslContext).build()
        client.register(JacksonFeature::class.java)
        client.register(MultiPartFeature::class.java)
    }
}