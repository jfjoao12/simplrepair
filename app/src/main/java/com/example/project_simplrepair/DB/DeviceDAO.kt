package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.project_simplrepair.Models.Device

/**
 * DAO for performing database operations related to [Device] entities.
 */
@Dao
interface DeviceDAO {

    /**
     * Inserts a [Device] into the database.
     * If a conflict occurs (e.g. same primary key), the insert will be ignored.
     *
     * @param device The device to insert.
     * @return The newly inserted row ID, or -1 if the insert was ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(device: Device): Long


    @Update
    suspend fun updateDevice(device: Device): Int

    /**
     * Retrieves the phone model name associated with a given device ID.
     *
     * @param id The device ID to search by.
     * @return The name of the associated phone model, or null if not found.
     */
    @Query("""
        SELECT name FROM phone_specs_table
        JOIN device_table ON device_table.phone_model_id = phone_specs_table.id
        WHERE device_table.id = :id
    """)
    fun getModelNameByDeviceId(id: Int): String?

    /**
     * Retrieves the [Device] associated with a repair.
     *
     * @param id The ID of the repair.
     * @return The [Device] linked to the given repair ID.
     */
    @Query("""
        SELECT * FROM device_table
        INNER JOIN repairs_table ON repairs_table.technician_id = device_table.id
        WHERE repairs_table.id = :id
    """)
    suspend fun getDeviceByRepairId(id: Int): Device

    @Query(
        """
            SELECT * FROM device_table
            WHERE id = :id
        """
    )
    suspend fun getDeviceById(id: Int): Device
}
