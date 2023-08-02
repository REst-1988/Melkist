package com.example.melkist.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import com.example.melkist.data.UserDataStore
import com.example.melkist.network.`interface`.ApiService
import com.example.melkist.utils.isOnline
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


private const val BASE_URL =
    "https://melkist.ir/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val okHttpClient = OkHttpClient().newBuilder() // TODO: CHECK IF CONNECTION TIME OUT IS OK
    .connectTimeout(120, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS)
    .writeTimeout(120, TimeUnit.SECONDS)
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