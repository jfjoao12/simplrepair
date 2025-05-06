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

    override suspend fun insertRepair(repair: Repair): Int =
        repairDAO.insert(repair).toInt()

    override suspend fun editRepair(repair: Repair): Int =
        repairDAO.updateRepair(repair)

    override suspend fun insertDevice(device: Device): Int =
        deviceDAO.insert(device).toInt()

    override suspend fun editDevice(device: Device): Int =
        deviceDAO.updateDevice(device)

    override suspend fun insertCustomer(customer: Customer): Int =
        customerDAO.insert(customer).toInt()

    override suspend fun editCustomer(customer: Customer): Int =
        customerDAO.updateCustomer(customer)

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
    @Relation(
        entity = Device::class,
        parentColumn = "device_id",
        entityColumn = "id"
    )
    val deviceWithSpecs: DeviceWithSpecs,

    @Relation(
        parentColumn = "id",
        entityColumn = "repair_id"
    )
    val photos: List<DevicePhoto>


)

data class DeviceWithSpecs(
    @Embedded
    val device: Device,

    @Relation(
        parentColumn  = "specs_id",  // the FK column in Device
        entityColumn  = "id"              // the PK column in DeviceSpecs
    )
    val specs: PhoneSpecs
)
