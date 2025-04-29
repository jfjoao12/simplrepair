package com.example.project_simplrepair.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhoneListResponseDTO (
    @Json(name="items")
    val items: List<PhoneDetailsDto>
)