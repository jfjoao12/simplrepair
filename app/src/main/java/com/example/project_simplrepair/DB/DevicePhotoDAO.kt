package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_simplrepair.Models.DevicePhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface DevicePhotoDAO {

    @Insert
    suspend fun insert(devicePhoto: DevicePhoto): Long

    @Query(
        """
            SELECT path FROM device_photos
        """
    )
    suspend fun getPhotosPath(): List<String>

    @Query("""
        UPDATE device_photos
        SET repair_id = :id
            """)
    fun updatePhotoRepairId(id: Int)

//    @Query("SELECT * FROM device_photos ORDER BY timestamp DESC")
//    fun getAllPhotos(): Flow<List<DevicePhoto>>
}