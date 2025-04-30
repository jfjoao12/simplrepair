package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import com.example.project_simplrepair.Models.Invoice

@Dao
interface InvoiceDAO {

    @Insert
    fun insert(invoice: Invoice)

}