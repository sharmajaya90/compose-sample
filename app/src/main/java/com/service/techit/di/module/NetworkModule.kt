package com.service.techit.di.module

import com.google.gson.GsonBuilder
import com.service.techit.utils.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [ContextModule::class,DataModule::class])
class NetworkModule {
    private var CONNECT_TIMEOUT: Long = 3 * 60L //establish connection with the server upto 30 seconds, by default 10 seconds.
    private var READ_TIMEOUT: Long = 3 * 60L
    private var WRITE_TIMEOUT: Long = 3 * 60L


    @Singleton
    @Provides
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .cache(null)
            .build()
    }

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://example.com")
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

}