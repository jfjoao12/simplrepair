package com.example.project_simplrepair.API

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneModels
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhonesApiManager(database: AppDatabase) {
    private var _phonesBrandsResponse = mutableStateOf<List<PhoneBrands>>(emptyList())
    private var _phonesModelsResponse = mutableStateOf<List<PhoneModels>>(emptyList())
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

    val phoneModelsResponse: MutableState<List<PhoneModels>>
        @Composable get() = remember{
            _phonesModelsResponse
        }
    init {
        getPhoneModels(database)
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhoneModels(database: AppDatabase) {
        GlobalScope.launch {  // Run in background thread
            val brands: List<PhoneBrands> = database.phoneBrandsDAO().getAll()
            var modelId = 0
            brands.map { brand ->
                val modelsService = Api.retrofitService.getModelsByBrand(brand.brandValue, api_key)

                modelsService.enqueue(object : Callback<List<PhoneModels>> {
                    override fun onResponse(
                        call: Call<List<PhoneModels>>,
                        response: Response<List<PhoneModels>>
                    ) {
                        GlobalScope.launch {
                            // Checking if table is empty before populating
                            if (response.isSuccessful && database.phoneModelsDAO().checkIfExists().isEmpty()) {
                                val models = response.body() ?: emptyList()
                                // Update the brandId for each model based on the current brand in the loop
                                val updatedModels = models.map { model ->
                                    model.copy(brandId = brand.id)
                                }
                                _phonesModelsResponse.value += updatedModels


                                    saveModelsToDatabase(database = database, phoneModels = _phonesModelsResponse.value)
                            } else {
                                Log.i("DataInfo", "phone_models_table not updated, already present")

                            }
                        }
                    }

                    override fun onFailure(call: Call<List<PhoneModels>>, t: Throwable) {
                        Log.d("error", "${t.message}")
                    }
                })
            }
        }
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