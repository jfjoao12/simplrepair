package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.PhoneModels

@Dao
interface PhoneModelsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(phoneModels: List<PhoneModels>)

    @Query("SELECT * FROM phone_models_table WHERE brand_id = :id")
    fun getBrandById(id: Int): PhoneModels?

    @Query("SELECT * FROM phone_models_table WHERE id = 124")
    fun checkIfExists(): List<PhoneModels>
}