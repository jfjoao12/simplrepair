package com.example.project_simplrepair.API

import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneBrandsItem
import com.example.project_simplrepair.Models.PhoneListResponseDTO
import com.example.project_simplrepair.Models.PhoneSpecsItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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
    ): Call<PhoneBrandsItem>

    /**
     * Retrieves detailed phone specifications for a specific brand and model.
     *
     * @param brandName The name of the phone brand.
     * @param modelName The model name of the phone.
     * @param apiKey The API key required to authenticate with the RapidAPI service.
     * @return A [Call] object for a [PhoneSpecsItems].
     */
    @GET("devices")
    fun getPhoneSpecifications(
        @Query("brandId") brandId: Int,
        @Query("pageSize") pageSize: String = "totalItems",
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<PhoneListResponseDTO>
}
