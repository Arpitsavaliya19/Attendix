package com.arpit.attendixapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.Lab
import com.arpit.attendixapp.repository.LabRepository
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LabsViewModel(private val labRepository: LabRepository) : ViewModel() {
    private val _allAvailableLabs = listOf(
        Lab(name = "Data Structures Lab", colorHex = "#FF5722"),
        Lab(name = "Operating Systems Lab", colorHex = "#2196F3"),
        Lab(name = "Database Management Systems Lab", colorHex = "#4CAF50"),
        Lab(name = "Computer Networks Lab", colorHex = "#9C27B0"),
        Lab(name = "Software Engineering Lab", colorHex = "#FFC107")
    )

    val myLabs: StateFlow<List<Lab>> = labRepository.getLabsByStudent(
        SessionManager.loggedInUserEmail ?: ""
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var availableLabs by mutableStateOf(_allAvailableLabs)
        private set

    init {
        viewModelScope.launch {
            myLabs.collect { added ->
                availableLabs = _allAvailableLabs.filter { available ->
                    added.none { it.name == available.name }
                }
            }
        }
    }

    fun addLab(lab: Lab) {
        val email = com.arpit.attendixapp.util.SessionManager.loggedInUserEmail ?: return
        viewModelScope.launch {
            labRepository.insertLab(lab, email)
        }
    }

    fun removeLab(lab: Lab) {
        viewModelScope.launch {
            labRepository.deleteLabByName(lab.name)
        }
    }

    fun markPresent(lab: Lab) {
        viewModelScope.launch {
            labRepository.markPresent(lab.id)
        }
    }

    fun markAbsent(lab: Lab) {
        viewModelScope.launch {
            labRepository.markAbsent(lab.id)
        }
    }
}
