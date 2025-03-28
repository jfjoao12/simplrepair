package com.example.project_simplrepair.API

import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneModels
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
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
}