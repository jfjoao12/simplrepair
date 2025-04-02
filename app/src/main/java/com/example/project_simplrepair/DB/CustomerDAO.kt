package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDAO {
    @Insert (onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(customer: Customer)

    @Query ("Select customer_name from customers_table WHERE id = :id")
    fun getNameById(id: Int): String

    @Query("SELECT * FROM customers_table")
    fun getAllNames(): Flow<List<Customer>>
}