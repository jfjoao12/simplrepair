package com.example.project_simplrepair.Models

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object Converters {
    private val moshi = Moshi.Builder().build()

    // For List<String>
    private val stringListAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )

    @TypeConverter
    fun fromStringList(list: List<String>): String =
        stringListAdapter.toJson(list)

    @TypeConverter fun toStringList(json: String): List<String> =
        stringListAdapter.fromJson(json) ?: emptyList()
}