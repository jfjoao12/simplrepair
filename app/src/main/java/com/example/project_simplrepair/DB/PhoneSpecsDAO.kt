package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.project_simplrepair.Models.PhoneSpecs

@Dao
interface PhoneSpecsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(phoneSpecifications: PhoneSpecs)


}