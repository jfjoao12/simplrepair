package com.example.project_simplrepair.API

import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneModels
import com.example.project_simplrepair.Models.PhoneSpecsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Retrofit service interface for communicating with the Mobile Phone Specs API.
 * Defines HTTP GET requests for retrieving phone brand data, models, and specifications.
 */
interface PhoneApiService {

    /**
     * Retrieves a list of all available phone brands.
     *
     * @param apiKey The API key required to authenticate with the RapidAPI service.
     * @return A [Call] object for a list of [PhoneBrands].
     */
    @GET("gsm/all-brands")
    fun getAllPhoneBrands(
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<List<PhoneBrands>>

    /**
     * Retrieves a list of phone models for a specific brand.
     *
     * @param brandName The name of the phone brand.
     * @param apiKey The API key required to authenticate with the RapidAPI service.
     * @return A [Call] object for a list of [PhoneModels].
     */
    @GET("gsm/get-models-by-brandname/{brand}")
    fun getModelsByBrand(
        @Path("brand") brandName: String,
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<List<PhoneModels>>

    /**
     * Retrieves detailed phone specifications for a specific brand and model.
     *
     * @param brandName The name of the phone brand.
     * @param modelName The model name of the phone.
     * @param apiKey The API key required to authenticate with the RapidAPI service.
     * @return A [Call] object for a [PhoneSpecsResponse].
     */
    @GET("gsm/get-specifications-by-brandname-modelname/{brand}/{model}")
    fun getPhoneSpecifications(
        @Path("brand") brandName: String,
        @Path("model") modelName: String,
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<PhoneSpecsResponse>
}
