package com.example.project_simplrepair.API

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneBrandsItem
import com.example.project_simplrepair.Models.PhoneListResponseDTO
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Models.PhoneSpecsItems
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
    private var _phonesModelsResponse = mutableStateOf<List<PhoneSpecs>>(emptyList())
    private var _phoneSpecsResponse = mutableStateOf<List<PhoneSpecs>>(emptyList())

    private val apiKey = "51bc23d70dmsh6429272287e5c73p18d54cjsn834e6269ba89"

    // Flag to prevent repeated fetch of models
    private var isModelsFetched = false
    private val db = database

    /**
     * A composable state holder for phone brands fetched from the API.
     */
    val phonesBrandsResponse: MutableState<List<PhoneBrands>>
        @Composable get() = remember { _phonesBrandsResponse }


    /**
     * Fetches the list of phone brands from the API, stores them in the database,
     * and triggers model fetching if not already done.
     */
    init {
        getPhonesBrands(db)
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhonesBrands(database: AppDatabase) {
        val service = Api.retrofitService.getAllPhoneBrands(apiKey)
        service.enqueue(object : Callback<PhoneBrandsItem> {
            override fun onResponse(
                call: Call<PhoneBrandsItem>,
                response: Response<PhoneBrandsItem>
            ) {
                GlobalScope.launch {
                    val brandsFromApi = response.body()?.item ?: emptyList()
                    if (response.isSuccessful && (brandsFromApi.size > database.phoneBrandsDAO().getAll().size)) {
                        _phonesBrandsResponse.value = brandsFromApi
                        database.phoneBrandsDAO().clearTable()
                        saveBrandsToDatabase(database, _phonesBrandsResponse.value)
                        if (!isModelsFetched) {
                            brandsFromApi.forEach { getPhoneSpecs(it.id, database) }
                            isModelsFetched = true
                        }
                    } else {
                        Log.i("DataInfo", "phone_brands table not updated, already present")
                    }
                }
            }

            override fun onFailure(call: Call<PhoneBrandsItem>, t: Throwable) {
                Log.d("error", "${t.message}")
            }
        })
    }

    /**
     * Fetches detailed specifications for a specific phone and stores it into the database.
     *
     * @param brandName The brand name of the phone.
     * @param modelName The model name of the phone.
     * @param database The Room database instance.
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun getPhoneSpecs(brandId: Int, database: AppDatabase) {
        val service = Api.retrofitService.getPhoneSpecifications(
            brandId = brandId,
            apiKey = apiKey
        )
        service.enqueue(object : Callback<PhoneListResponseDTO> {
            override fun onResponse(
                call: Call<PhoneListResponseDTO>,
                response: Response<PhoneListResponseDTO>
            ) {
                GlobalScope.launch {
                    if (response.isSuccessful && response.body() != null) {
                        val specsResponse = response.body()!!.items
                        specsResponse.forEach {specs ->
                            val phoneSpecs = PhoneSpecs(
                                id = specs.id,
                                name = specs.name,
                                brand = specs.brand,
                                battery = specs.battery,
                                platform = specs.platform,
                                network = specs.network,
                                display = specs.display,
                                launch = specs.launch,
                                memory = specs.memory,
                                comms = specs.comms,
                                features = specs.features,
                                colors = specs.colors,
                                models = specs.models,
                                mainCamera = specs.cameras?.mainCamera,
                                selfieCamera = specs.cameras?.selfieCamera,
                            )
                            Log.i("PhoneSpecs", specs.toString())
                            _phoneSpecsResponse.value = listOf(phoneSpecs)
                            database.phoneSpecsDAO().insert(phoneSpecs)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<PhoneListResponseDTO>, t: Throwable) {

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
    private suspend fun saveModelsToDatabase(database: AppDatabase, phoneModels: List<PhoneSpecs>) {
        database.phoneModelsDAO().insertAll(phoneModels)
    }
}
