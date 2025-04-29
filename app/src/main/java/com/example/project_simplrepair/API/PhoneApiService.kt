package com.example.project_simplrepair.API

import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.Phones
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
    @GET("brands?pageSize=totalItems")
    fun getAllPhoneBrands(
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<List<PhoneBrands>>

    /**
     * Retrieves a list of phone models for a specific brand.
     *
     * @param brandName The name of the phone brand.
     * @param apiKey The API key required to authenticate with the RapidAPI service.
     * @return A [Call] object for a list of [Phones].
     */
    @GET("devices?brandId={brand-id}&pageSize=totalItems")
    fun getModelsByBrand(
        @Path("brand-id") brandId: Int,
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<List<Phones>>

}
