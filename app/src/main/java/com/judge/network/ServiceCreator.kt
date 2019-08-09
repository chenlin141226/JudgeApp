package com.judge.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    const val BASE_URL = "http://bbs.caipanshuo.com"
    private const val CONNECT_TIMEOUT = 10L
    private const val READ_TIMEOUT = 20L
    private const val WRITE_TIMEOUT = 20L
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(LogInterceptor())
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())


    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}