package com.example.project_simplrepair.API

import com.example.project_simplrepair.Models.PhoneBrands
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header

interface PhoneApiService {
    @GET("gsm/all-brands")
    fun getAllPhoneBrands(@Header("X-RapidAPI-Key") apiKey: String): Call<List<PhoneBrands>>
}