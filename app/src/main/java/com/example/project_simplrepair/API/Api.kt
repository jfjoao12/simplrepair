package com.example.project_simplrepair.API

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton object that sets up the Retrofit client for communicating
 * with the Mobile Phone Specs API.
 *
 * This object initializes Retrofit with:
 * - A base URL for the API
 * - A Moshi converter for JSON serialization
 * - An instance of [PhoneApiService] for making requests
 */
object Api {

    // Base URL for the external phone specifications API
    private const val BASE_URL = "https://mobile-devices-api1.p.rapidapi.com/"

    // Moshi instance configured with Kotlin support
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Retrofit instance configured with Moshi and base URL
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Lazily-initialized instance of [PhoneApiService] used to perform API calls.
     */
    val retrofitService: PhoneApiService by lazy {
        retrofit.create(PhoneApiService::class.java)
    }
}
