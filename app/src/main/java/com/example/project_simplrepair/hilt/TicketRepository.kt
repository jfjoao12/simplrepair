package com.example.project_simplrepair.hilt

import com.example.project_simplrepair.DB.RepairDAO
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.Repair
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TicketRepository {
    // Get full ticket
    fun getAllRepairs(): Flow<List<Repair>>
    fun getFullTicket(id: Int): Flow<FullTicket>
    fun getAllFullTickets(): Flow<List<FullTicket>>

    // Repair
    suspend fun insertRepair(repair: Repair): Int
    suspend fun updateRepair(repair: Repair): Int

    // Device
    suspend fun insertDevice(device: Device): Int
    suspend fun updateDevice(device: Device): Int

    // Customer
    suspend fun insertCustomer(customer: Customer): Int
    suspend fun updateCustomer(customer: Customer): Int
    suspend fun getCustomerById(id: Int): Customer

    // Device photos
    suspend fun insertDevicePhoto(devicePhoto: DevicePhoto): Int
    //suspend fun updateDevicePhoto(devicePhoto: DevicePhoto): Int


}