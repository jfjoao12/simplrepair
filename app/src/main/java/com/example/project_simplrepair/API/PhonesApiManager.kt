package com.example.project_simplrepair.API

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.PhoneBrands
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PhonesApiManager(database: AppDatabase) {
    private var _phonesBrandsResponse = mutableStateOf<List<PhoneBrands>>(emptyList())

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

        service.enqueue(object : retrofit2.Callback<List<PhoneBrands>> {
            override fun onResponse(
                call: Call<List<PhoneBrands>>,
                response: Response<List<PhoneBrands>>
            ) {
                if (response.isSuccessful) {
                    Log.i("Data", "Data is locked and loaded.")
                    _phonesBrandsResponse.value = response.body() ?: emptyList()
                    Log.i("DataStream", _phonesBrandsResponse.value.toString())

                    // Save data to the database
                    GlobalScope.launch {
                        saveDataToDatabase(database = database, phoneBrands = _phonesBrandsResponse.value)
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
    private suspend fun saveDataToDatabase(database: AppDatabase, phoneBrands: List<PhoneBrands>) {
        database.phoneBrandsDAO().insertAll(phoneBrands)
    }
}