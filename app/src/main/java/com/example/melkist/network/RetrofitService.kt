package com.example.melkist.network


import com.example.melkist.network.`interface`.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


private const val BASE_URL =
    "https://test.melkist.net/api/"
    /*"https://panel.melkist.net/api/"*/ // TODO: uncomment for main version

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val okHttpClient = OkHttpClient().newBuilder()
    .connectTimeout(180, TimeUnit.SECONDS)
    .readTimeout(180, TimeUnit.SECONDS)
    .writeTimeout(180, TimeUnit.SECONDS)
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()


object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}