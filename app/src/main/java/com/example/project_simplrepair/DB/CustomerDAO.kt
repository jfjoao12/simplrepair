package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Customer
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing and manipulating customer data.
 */
@Dao
interface CustomerDAO {

    /**
     * Inserts a new customer into the database.
     *
     * @param customer The customer to insert.
     * @throws SQLiteConstraintException if a conflict occurs.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(customer: Customer)

    /**
     * Retrieves a customer's name by their ID.
     *
     * @param id The ID of the customer.
     * @return The name of the customer.
     */
    @Query("SELECT customer_name FROM customers_table WHERE id = :id")
    fun getNameById(id: Int): String

    /**
     * Returns a flow of all customers in the database.
     *
     * @return A Flow emitting a list of all customers.
     */
    @Query("SELECT * FROM customers_table")
    fun getAllNames(): Flow<List<Customer>>

    /**
     * Searches for customers whose name contains the given query string.
     *
     * @param customerName The partial name to search for.
     * @return A Flow emitting a list of matching customers.
     */
    @Query("SELECT * FROM customers_table WHERE customer_name LIKE '%' || :customerName || '%'")
    fun getCustomerByName(customerName: String): Flow<List<Customer>>

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

    @Query(
        """
            SELECT * FROM customers_table
            WHERE id = :id
        """
    )
    suspend fun getCustomerById(id: Int): Customer
}
