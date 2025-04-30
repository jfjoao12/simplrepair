package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.RepairStatus
import com.example.project_simplrepair.Models.Technician

/**
 * Data Access Object for the [Repair] entity.
 * Provides methods to insert and retrieve repair records,
 * along with their related [Customer], [Technician], and [Device] entities.
 */
@Dao
interface RepairDAO {

    /**
     * Inserts a [Repair] into the database.
     * Fails if a conflict occurs (e.g., duplicate ID).
     *
     * @param repair The repair record to insert.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(repair: Repair): Long

    /**
     * Retrieves a repair by its ID.
     *
     * @param id The ID of the repair.
     * @return The [Repair] object or null if not found.
     */
    @Query("SELECT * FROM repairs_table WHERE id = :id")
    suspend fun getRepairById(id: Int): Repair?

    /**
     * Retrieves all repairs.
     *
     * @return A list of all [Repair] entries.
     */
    @Query("SELECT * FROM repairs_table")
    suspend fun getAllRepairs(): List<Repair>

    /**
     * Retrieves the [Customer] associated with a repair.
     *
     * @param id The ID of the repair.
     * @return The [Customer] linked to the given repair ID.
     */
    @Query("""
        SELECT * FROM customers_table 
        INNER JOIN repairs_table ON repairs_table.customer_id = customers_table.id
        WHERE repairs_table.id = :id
    """)
    suspend fun getCustomerByRepairId(id: Int): Customer

    /**
     * Retrieves the [Technician] associated with a repair.
     *
     * @param id The ID of the repair.
     * @return The [Technician] linked to the given repair ID.
     */
    @Query("""
        SELECT * FROM technician_table 
        INNER JOIN repairs_table ON repairs_table.technician_id = technician_table.id
        WHERE repairs_table.id = :id
    """)
    suspend fun getTechByRepairId(id: Int): Technician

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

    @Query ("""
        UPDATE repairs_table
        SET repair_status = :newStatus
        WHERE id = :id
    """)
    suspend fun closeRepairByid(id: Int, newStatus: RepairStatus)

    @Query (
        """
            SELECT * 
            FROM repairs_table
            WHERE id LIKE '%' || :searchParam || '%'
        """
    )
    suspend fun searchRepairById(searchParam: Int): List<Repair>
}
