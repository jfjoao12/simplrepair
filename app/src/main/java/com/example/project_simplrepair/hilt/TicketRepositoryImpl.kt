package com.example.project_simplrepair.hilt

import androidx.room.Embedded
import androidx.room.Relation
import com.example.project_simplrepair.DB.CustomerDAO
import com.example.project_simplrepair.DB.DeviceDAO
import com.example.project_simplrepair.DB.RepairDAO
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TicketRepositoryImpl.kt
class TicketRepositoryImpl @Inject constructor(
    private val repairDAO: RepairDAO,
    private val customerDAO: CustomerDAO,
    private val deviceDAO: DeviceDAO
) : TicketRepository {
    override fun getAllRepairs(): Flow<List<Repair>> =
        repairDAO.getAllRepairs()

    override fun getFullTicket(id: Int): Flow<FullTicket> =
        repairDAO.streamFullTicketById(id)

    override fun getAllFullTickets(): Flow<List<FullTicket>> =
        repairDAO.streamAllFullTickets()
}

data class FullTicket(
    @Embedded
    val repair: Repair,

    @Relation(
        parentColumn  = "id",
        entityColumn  = "id"
    )
    val customer: Customer,

    // embed our DeviceWithSpecs instead of a raw Device:
    @Embedded(prefix = "fulldevice_")
    val deviceWithSpecs: DeviceWithSpecs
)

data class DeviceWithSpecs(
    @Embedded
    val device: Device,

    @Relation(
        parentColumn  = "fulldevice_specs_id",  // the FK column in Device
        entityColumn  = "id"              // the PK column in DeviceSpecs
    )
    val specs: PhoneSpecs
)
