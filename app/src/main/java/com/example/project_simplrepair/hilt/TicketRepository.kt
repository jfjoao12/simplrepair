package com.example.project_simplrepair.hilt

import com.example.project_simplrepair.DB.RepairDAO
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TicketRepository {
    fun getAllRepairs(): Flow<List<Repair>>
    fun getFullTicket(id: Int): Flow<FullTicket>
    fun getAllFullTickets(): Flow<List<FullTicket>>

    // Repair
    suspend fun insertRepair(repair: Repair): Int
    suspend fun editRepair(repair: Repair): Int

    // Device
    suspend fun insertDevice(device: Device): Int
    suspend fun editDevice(device: Device): Int

    // Customer
    suspend fun insertCustomer(customer: Customer): Int
    suspend fun editCustomer(customer: Customer): Int

}