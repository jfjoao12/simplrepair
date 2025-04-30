package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_simplrepair.Models.Inventory
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryItemDAO {
    @Insert
    fun insert(inventory: Inventory)

    @Query
        ("""
            SELECT * FROM inventory_table
            WHERE name LIKE '%' || :search || '%'
        """)
    fun search(search: String): List<Inventory>

    @Query
        ("""
            UPDATE inventory_table
            SET repair_id = :repairId
            WHERE id = :itemId
        """)
    fun updateItemByRepairId(repairId: Int, itemId: Int)
}