package com.example.project_simplrepair.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Operations.RepairType

class InsertRepairViewModel : ViewModel() {
    var modelName by   mutableStateOf("")
    var serial by   mutableStateOf("")
    var customerName by   mutableStateOf("")
    var customerId by   mutableIntStateOf(0)
    var phoneSpecs by   mutableStateOf<PhoneSpecs?>(null)
    var phoneSpecsId by mutableIntStateOf(0)
    var deviceIsSelected by mutableStateOf(false)
    var phoneColor by mutableStateOf("")
    var price by   mutableStateOf("")
    var selectedType by   mutableStateOf(RepairType.BATTERY)
    var modelBrand by   mutableStateOf("Model")
    var notes by mutableStateOf("")
}