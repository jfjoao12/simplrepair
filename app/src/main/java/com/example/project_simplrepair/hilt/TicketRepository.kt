package com.example.project_simplrepair.hilt

import com.example.project_simplrepair.DB.RepairDAO
import com.example.project_simplrepair.Models.Repair
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TicketRepository {
    fun getAllRepairs(): Flow<List<Repair>>
    fun getFullTicket(id: Int): Flow<FullTicket>
    fun getAllFullTickets(): Flow<List<FullTicket>>

}