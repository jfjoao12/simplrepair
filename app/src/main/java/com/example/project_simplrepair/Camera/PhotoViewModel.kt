package com.example.project_simplrepair.Camera

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PhotoViewModel : ViewModel() {
    // A Compose-friendly snapshot list
    val photoPaths = mutableStateListOf<String>()

    fun add(path: String) {
        photoPaths += path
    }

}