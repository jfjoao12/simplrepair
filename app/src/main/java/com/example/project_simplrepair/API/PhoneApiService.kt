package com.example.project_simplrepair.API

import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneModels
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Models.PhoneSpecsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path



interface PhoneApiService {
    @GET("gsm/all-brands")
    fun getAllPhoneBrands(
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<List<PhoneBrands>>

    @GET("gsm/get-models-by-brandname/{brand}")
    fun getModelsByBrand(
        @Path("brand") brandName: String,
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<List<PhoneModels>>

    @GET("gsm/get-specifications-by-brandname-modelname/{brand}/{model}")
    fun getPhoneSpecifications(
        @Path("brand") brandName: String,
        @Path("model") modelName: String,
        @Header("X-RapidAPI-Key") apiKey: String
    ): Call<PhoneSpecsResponse>
}