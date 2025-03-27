package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.PhoneBrands

@Dao
interface PhoneBrandsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(phoneBrands: List<PhoneBrands>)

    @Query("SELECT * FROM phone_brands WHERE brand_id = :id")
    fun getBrandById(id: Int): PhoneBrands?
}