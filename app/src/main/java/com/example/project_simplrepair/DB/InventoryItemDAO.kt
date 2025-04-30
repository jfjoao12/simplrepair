package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_simplrepair.Models.Inventory

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
}