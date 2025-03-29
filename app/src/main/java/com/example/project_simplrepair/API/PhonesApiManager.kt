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

    val phonesBrandsResponse: MutableState<List<PhoneBrands>>
        @Composable get() = remember{
            _phonesBrandsResponse
        }
    init {
        getPhonesBrands(database)
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhonesBrands(database: AppDatabase){
        val service = Api.retrofitService.getAllPhoneBrands(api_key)
        service.enqueue(object : Callback<List<PhoneBrands>> {
            override fun onResponse(
                call: Call<List<PhoneBrands>>,
                response: Response<List<PhoneBrands>>
            ) {
                GlobalScope.launch {
                    if (response.isSuccessful && (response.body()?.size!! > database.phoneBrandsDAO().getAll().size) ) {
                        Log.i("Data", "Data is locked and loaded.")
                        _phonesBrandsResponse.value = response.body() ?: emptyList()
                        Log.i("DataStream", _phonesBrandsResponse.value.toString())

                        database.phoneBrandsDAO().clearTable()
                        saveBrandsToDatabase(database = database, phoneBrands = _phonesBrandsResponse.value)
                        getPhoneModels(database) // call api right after this one runs
                    } else {
                        Log.i("DataInfo", "phone_brands table not updated, already present")
                    }
                }
            }

            override fun onFailure(
                call: Call<List<PhoneBrands>>,
                t: Throwable
            ) {
                Log.d("error", "${t.message}")
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhoneModels(database: AppDatabase) {
        GlobalScope.launch {  // Run in background thread
            val brands: List<PhoneBrands> = database.phoneBrandsDAO().getAll()
            var modelId = 0
            if (database.phoneModelsDAO().checkIfExists().isEmpty()) {
                brands.map { brand ->
                    val modelsService = Api.retrofitService.getModelsByBrand(brand.brandValue, api_key)
                    modelsService.enqueue(object : Callback<List<PhoneModels>> {
                        override fun onResponse(
                            call: Call<List<PhoneModels>>,
                            response: Response<List<PhoneModels>>
                        ) {
                            GlobalScope.launch {
                                // Checking if table is empty before populating
                                if (response.isSuccessful) {
                                    val models = response.body() ?: emptyList()
                                    // Update the brandId for each model based on the current brand in the loop
                                    val updatedModels = models.map { model ->
                                        model.copy(brandId = brand.id)
                                    }
                                    _phonesModelsResponse.value += updatedModels


                                    saveModelsToDatabase(database = database, phoneModels = _phonesModelsResponse.value)
                                }
                            }
                        }
                        override fun onFailure(call: Call<List<PhoneModels>>, t: Throwable) {
                            Log.d("error", "${t.message}")
                        }
                    })
                }
            } else {
                Log.i("DataInfo", "phone_models_table not updated, already present")
                return@launch
            }
        }
    }

//    val phoneSpecsResponse: MutableState<PhoneSpecs?>
//        @Composable get() = remember {
//            _phoneSpecsResponse
//        }
//
//    init {
//        getPhoneSpecs("Apple", "iPhone 16 Pro Max", database)
//    }
    @OptIn(DelicateCoroutinesApi::class)
    fun getPhoneSpecs(brandName: String, modelName: String, database: AppDatabase) {
        val service = Api.retrofitService.getPhoneSpecifications(brandName, modelName, api_key)
        service.enqueue(object : Callback<PhoneSpecsResponse> {
            override fun onResponse(
                call: Call<PhoneSpecsResponse>,
                response: Response<PhoneSpecsResponse>
            ) {
                GlobalScope.launch {
                    if (response.isSuccessful) {
                        if (response.body()!= null) {
                            val phoneSpecs = PhoneSpecs(
                                year = response.body()!!.phoneDetails.year,
                                brand = response.body()!!.phoneDetails.brand,
                                modelName = response.body()!!.phoneDetails.modelName,
                                launchDate = response.body()!!.launchDetails.launchDate,
                                chipset = response.body()!!.gsmPlatformDetails.chipset,
                                cpu = response.body()!!.gsmPlatformDetails.cpu,
                                gpu = response.body()!!.gsmPlatformDetails.gpu,
                                mainCameraFeatures = response.body()!!.gsmMainCameraDetails.mainCamFeatures,
                                mainCameraSpecs = response.body()!!.gsmMainCameraDetails.mainCameraQuad
                                    ?: response.body()!!.gsmMainCameraDetails.mainCameraTriple
                                    ?: "N/A",
                                mainCameraVideo = response.body()!!.gsmMainCameraDetails.mainCameraVideo
                            )
                            _phoneSpecsResponse.value = phoneSpecs
                            Log.i("PhoneSpecs", "Fetched specs for $brandName $modelName: $phoneSpecs")

                            // Optionally, save to the database here
                            database.phoneSpecsDAO().insert(phoneSpecs)
                        } else {
                            Log.i("PhoneSpecs", "Response body is null for $brandName $modelName.")
                        }
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

//    val phoneModelsResponse: MutableState<List<PhoneBrands>>
//        @Composable get() = remember {
//            _phonesModelsResponse
//        }
//    init {
//        getPhoneModels(database)
//    }
//    @OptIn(DelicateCoroutinesApi::class)
//    private fun getPhoneModels(database: AppDatabase) {
//        val service = api.retro
//    }

    private suspend fun saveBrandsToDatabase(database: AppDatabase, phoneBrands: List<PhoneBrands>) {
        database.phoneBrandsDAO().insertAll(phoneBrands)
    }

    private suspend fun saveModelsToDatabase(database: AppDatabase, phoneModels: List<PhoneModels>){
        database.phoneModelsDAO().insertAll(phoneModels)
    }
}