package com.example.unsplash.sys.di.module

import android.annotation.SuppressLint
import com.example.unsplash.data.datasource.web.api.WebServices
import com.example.unsplash.sys.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    private const val secondsToWait = 300


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }


    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return try {
            @SuppressLint("CustomX509TrustManager") val x509TrustManager: X509TrustManager =
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                }
            val trustAllCerts = arrayOf<TrustManager>(x509TrustManager)
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .sslSocketFactory(sslContext.socketFactory, x509TrustManager)
                .hostnameVerifier { _: String?, _: SSLSession? -> true }
                .connectTimeout(secondsToWait.toLong(), TimeUnit.SECONDS)
                .writeTimeout(secondsToWait.toLong(), TimeUnit.SECONDS)
                .readTimeout(secondsToWait.toLong(), TimeUnit.SECONDS).build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }




    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideWebService(retrofit: Retrofit): WebServices {
        return retrofit.create(WebServices::class.java)
    }
}