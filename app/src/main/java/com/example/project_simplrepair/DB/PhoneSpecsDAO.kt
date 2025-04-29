package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.PhoneSpecs

/**
 * DAO for managing phone specification entries in the database.
 */
@Dao
interface PhoneSpecsDAO {

    /**
     * Inserts a [PhoneSpecs] record into the database.
     * If the record already exists (based on primary key), it will be replaced.
     *
     * @param phoneSpecifications The phone specifications to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(phoneSpecifications: PhoneSpecs)


}
