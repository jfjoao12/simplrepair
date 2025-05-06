package com.example.project_simplrepair.hilt

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_simplrepair.Models.Repair
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

}
