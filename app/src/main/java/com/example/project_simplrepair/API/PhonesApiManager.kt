package com.example.project_simplrepair.API

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.Phones
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Models.PhoneSpecsResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Manages interaction with the mobile phone specs API and handles data persistence into Room database.
 * Automatically fetches and stores brands and models if needed.
 *
 * @param database The Room database instance for local data operations.
 */
class PhonesApiManager(database: AppDatabase) {

    private var _phonesBrandsResponse = mutableStateOf<List<PhoneBrands>>(emptyList())
    private var _phonesModelsResponse = mutableStateOf<List<Phones>>(emptyList())
    //private var _phoneSpecsResponse = mutableStateOf<PhoneSpecs?>(null)

    val api_key = "51bc23d70dmsh6429272287e5c73p18d54cjsn834e6269ba89"

    // Flag to prevent repeated fetch of models
    private var isModelsFetched = false

    /**
     * A composable state holder for phone brands fetched from the API.
     */
    val phonesBrandsResponse: MutableState<List<PhoneBrands>>
        @Composable get() = remember { _phonesBrandsResponse }

    init {
        getPhonesBrands(database)
    }

    /**
     * Fetches the list of phone brands from the API, stores them in the database,
     * and triggers model fetching if not already done.
     */
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
                    if (response.isSuccessful && (brandsFromApi.size > database.phoneBrandsDAO().getAll().size)) {
                        _phonesBrandsResponse.value = brandsFromApi
                        database.phoneBrandsDAO().clearTable()
                        saveBrandsToDatabase(database, _phonesBrandsResponse.value)
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

    /**
     * Fetches all phone models for each brand from the API and inserts them into the database.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhoneModels(database: AppDatabase) {
        GlobalScope.launch {
            if (isModelsFetched) {
                Log.i("PhoneModels", "getPhoneModels has already been called.")
                return@launch
            }
            isModelsFetched = true

            val brands = database.phoneBrandsDAO().getAll()
            if (database.phoneModelsDAO().checkIfExists() < 1) {
                brands.forEach { brand ->
                    val modelsService = Api.retrofitService.getModelsByBrand(brand.brandValue, api_key)
                    modelsService.enqueue(object : Callback<List<Phones>> {
                        override fun onResponse(
                            call: Call<List<Phones>>,
                            response: Response<List<Phones>>
                        ) {
                            GlobalScope.launch {
                                if (response.isSuccessful) {
                                    val models = response.body() ?: emptyList()
                                    val updatedModels = models.map { it.copy(brandId = brand.id) }
                                    _phonesModelsResponse.value += updatedModels
                                    saveModelsToDatabase(database, updatedModels)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Phones>>, t: Throwable) {
                            Log.d("error", "${t.message}")
                        }
                    })
                }
            } else {
                Log.i("PhoneModels", "phone_models_table not updated, already present")
            }
        }
    }

    /**
     * Fetches detailed specifications for a specific phone and stores it into the database.
     *
     * @param brandName The brand name of the phone.
     * @param modelName The model name of the phone.
     * @param database The Room database instance.
     */
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
                        database.phoneSpecsDAO().insert(phoneSpecs)
                    }
                }
            }

            override fun onFailure(call: Call<PhoneSpecsResponse>, t: Throwable) {
                Log.d("error", "Failed to fetch phone specs: ${t.message}")
            }
        })
    }

    /**
     * Inserts the provided list of phone brands into the local database.
     */
    private suspend fun saveBrandsToDatabase(database: AppDatabase, phoneBrands: List<PhoneBrands>) {
        database.phoneBrandsDAO().insertAll(phoneBrands)
    }

    /**
     * Inserts the provided list of phone models into the local database.
     */
    private suspend fun saveModelsToDatabase(database: AppDatabase, phoneModels: List<Phones>) {
        database.phoneModelsDAO().insertAll(phoneModels)
    }
}
