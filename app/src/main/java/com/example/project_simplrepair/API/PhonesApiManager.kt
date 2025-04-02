package com.example.project_simplrepair.API

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneModels
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Models.PhoneSpecsResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhonesApiManager(database: AppDatabase) {
    private var _phonesBrandsResponse = mutableStateOf<List<PhoneBrands>>(emptyList())
    private var _phonesModelsResponse = mutableStateOf<List<PhoneModels>>(emptyList())
    private var _phoneSpecsResponse = mutableStateOf<PhoneSpecs?>(null)
    val api_key = "0dc1c9bf90msh8536d2155c57902p1798e8jsn2d2878a46a1b"

    // Flag to ensure getPhoneModels only runs once
    private var isModelsFetched = false

    val phonesBrandsResponse: MutableState<List<PhoneBrands>>
        @Composable get() = remember {
            _phonesBrandsResponse
        }
    init {
        getPhonesBrands(database)
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhonesBrands(database: AppDatabase) {
        val service = Api.retrofitService.getAllPhoneBrands(api_key)
        service.enqueue(object : Callback<List<PhoneBrands>> {
            override fun onResponse(
                call: Call<List<PhoneBrands>>,
                response: Response<List<PhoneBrands>>
            ) {
                GlobalScope.launch {
                    val brandsFromApi = response.body() ?: emptyList()
                    // Check if the response has more items than in the local database.
                    if (response.isSuccessful && (brandsFromApi.size > database.phoneBrandsDAO().getAll().size)) {
                        Log.i("Data", "Data is locked and loaded.")
                        _phonesBrandsResponse.value = brandsFromApi
                        Log.i("DataStream", _phonesBrandsResponse.value.toString())

                        database.phoneBrandsDAO().clearTable()
                        saveBrandsToDatabase(database, _phonesBrandsResponse.value)
                        // Call getPhoneModels only if not already called.
                        getPhoneModels(database)
                    } else {
                        Log.i("DataInfo", "phone_brands table not updated, already present")
                    }
                }
            }

            override fun onFailure(call: Call<List<PhoneBrands>>, t: Throwable) {
                Log.d("error", "${t.message}")
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhoneModels(database: AppDatabase) {
        GlobalScope.launch {  // Run in background thread
            // Use the flag to prevent repeated calls.
            if (isModelsFetched) {
                Log.i("PhoneModels", "getPhoneModels has already been called.")
                return@launch
            }
            isModelsFetched = true

            val brands: List<PhoneBrands> = database.phoneBrandsDAO().getAll()
            // Check if there are any records in the phone_models_table.
            if (database.phoneModelsDAO().checkIfExists() < 1) {
                brands.forEach { brand ->
                    val modelsService = Api.retrofitService.getModelsByBrand(brand.brandValue, api_key)
                    modelsService.enqueue(object : Callback<List<PhoneModels>> {
                        override fun onResponse(
                            call: Call<List<PhoneModels>>,
                            response: Response<List<PhoneModels>>
                        ) {
                            GlobalScope.launch {
                                if (response.isSuccessful) {
                                    val models = response.body() ?: emptyList()
                                    // Update each model with the brandId from the current brand.
                                    val updatedModels = models.map { model ->
                                        model.copy(brandId = brand.id)
                                    }
                                    // Append new models to the current state.
                                    _phonesModelsResponse.value += updatedModels
                                    Log.i("PhoneModels", "Fetched models for brand: ${brand.brandValue}")

                                    // Save the models to the database.
                                    saveModelsToDatabase(database, updatedModels)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<PhoneModels>>, t: Throwable) {
                            Log.d("error", "${t.message}")
                        }
                    })
                }
            } else {
                Log.i("PhoneModels", "phone_models_table not updated, already present")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getPhoneSpecs(brandName: String, modelName: String, database: AppDatabase) {
        val service = Api.retrofitService.getPhoneSpecifications(brandName, modelName, api_key)
        service.enqueue(object : Callback<PhoneSpecsResponse> {
            override fun onResponse(
                call: Call<PhoneSpecsResponse>,
                response: Response<PhoneSpecsResponse>
            ) {
                GlobalScope.launch {
                    if (response.isSuccessful && response.body() != null) {
                        val specs = response.body()!!
                        val phoneSpecs = PhoneSpecs(
                            year = specs.phoneDetails.year,
                            brand = specs.phoneDetails.brand,
                            modelName = specs.phoneDetails.modelName,
                            launchDate = specs.launchDetails.launchDate,
                            chipset = specs.gsmPlatformDetails.chipset,
                            cpu = specs.gsmPlatformDetails.cpu,
                            gpu = specs.gsmPlatformDetails.gpu,
                            mainCameraFeatures = specs.gsmMainCameraDetails.mainCamFeatures,
                            mainCameraSpecs = specs.gsmMainCameraDetails.mainCameraQuad
                                ?: specs.gsmMainCameraDetails.mainCameraTriple
                                ?: "N/A",
                            mainCameraVideo = specs.gsmMainCameraDetails.mainCameraVideo
                        )
                        _phoneSpecsResponse.value = phoneSpecs
                        Log.i("PhoneSpecs", "Fetched specs for $brandName $modelName: $phoneSpecs")

                        // Optionally, save to the database.
                        database.phoneSpecsDAO().insert(phoneSpecs)
                    } else {
                        Log.i("PhoneSpecs", "No specs found for $brandName $modelName. Response code: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<PhoneSpecsResponse>, t: Throwable) {
                Log.d("error", "Failed to fetch phone specs: ${t.message}")
            }
        })
    }

    private suspend fun saveBrandsToDatabase(database: AppDatabase, phoneBrands: List<PhoneBrands>) {
        database.phoneBrandsDAO().insertAll(phoneBrands)
    }

    private suspend fun saveModelsToDatabase(database: AppDatabase, phoneModels: List<PhoneModels>) {
        database.phoneModelsDAO().insertAll(phoneModels)
    }
}
