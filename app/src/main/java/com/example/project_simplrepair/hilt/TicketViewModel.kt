package com.example.project_simplrepair.hilt

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.ViewModels.PhotoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
): ViewModel() {
    private val photoVm: PhotoViewModel = PhotoViewModel()
    private val _repairs = MutableStateFlow<List<Repair>>(emptyList())
    val repairs: StateFlow<List<Repair>> = _repairs

    init {
        viewModelScope.launch {
            ticketRepository.getAllRepairs()
                .collect { list ->
                    _repairs.value = list
                }
        }
    }

    private val _fullTicket = MutableStateFlow<FullTicket?>(null)
    val fullTicket: StateFlow<FullTicket?> = _fullTicket

    fun loadFullTicket(id: Int) {
        viewModelScope.launch {
            // collect the Flow so you get the first (and only) emission
            ticketRepository.getFullTicket(id)
                .collect { ticket ->
                    _fullTicket.value = ticket
                }
        }
    }

    // **all** FullTickets
    val fullTickets = ticketRepository.getAllFullTickets()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // INSERT
    fun insertNewRepair(
        repair: Repair,
        device: Device,
        customerId: Int,
        photoPaths: List<String>
    ) = viewModelScope.launch(Dispatchers.IO) {
        val newDeviceId = ticketRepository.insertDevice(device)
        val updatedRepair = repair.copy(deviceId = newDeviceId, customerId = customerId)
        val newRepairId = ticketRepository.insertRepair(updatedRepair)

        photoPaths.forEach { path ->
            val devicePhoto = DevicePhoto(
                photoId = null,
                repairId = newRepairId,
                filePath = path,
            )
            ticketRepository.insertDevicePhoto(devicePhoto)
        }

        // now you have newRepairId and can—for example—navigate to details
    }

}
