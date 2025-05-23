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
        WHERE path = :path
            """)
    fun updatePhotoRepairId(id: Int, path: String)


        /**
         * Return all file‐paths for the photos attached to a given repair.
         */
        @Query("""
            SELECT dp.path
              FROM device_photos AS dp
              INNER JOIN repairs_table AS r
                ON dp.repair_id = r.id
             WHERE r.id = :repairId
          """)
        suspend fun getPhotoPathsForRepair(repairId: Int): List<String>


//    @Query("SELECT * FROM device_photos ORDER BY timestamp DESC")
//    fun getAllPhotos(): Flow<List<DevicePhoto>>
}