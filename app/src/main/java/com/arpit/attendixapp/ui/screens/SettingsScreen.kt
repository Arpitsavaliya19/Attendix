package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.util.SessionManager
import com.arpit.attendixapp.viewmodels.SettingsViewModel

class SettingsViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                application,
                application.subjectRepository,
                application.labRepository,
                application.studentRepository,
                application.staffRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(application)
    )

    Column(modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)) {
        // General Section
        Text(
            text = "General",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        SettingItem(
            title = "Set Criteria (${SessionManager.attendanceCriteria.toInt()}%)",
            icon = Icons.AutoMirrored.Filled.PlaylistAddCheck
        ) { 
            settingsViewModel.showCriteriaDialog = true 
        }
        SettingItem(title = "Set Theme", icon = Icons.Filled.Palette) { /* Placeholder for future theme implementation */ }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Database Section
        Text(
            text = "Database",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        SettingItem(title = "Backup", icon = Icons.Filled.Backup) { /* TODO */ }
        SettingItem(title = "Restore", icon = Icons.Filled.Restore) { /* TODO */ }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Reset Section
        Text(
            text = "Reset",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        SettingItem(title = "Reset Subjects", icon = Icons.Filled.DeleteSweep) { settingsViewModel.resetSubjects() }
        SettingItem(title = "Reset Data", icon = Icons.Filled.DeleteForever) { settingsViewModel.resetData() }
        SettingItem(title = "Reset App", icon = Icons.Filled.RestartAlt) { settingsViewModel.resetApp() }
    }

    if (settingsViewModel.showCriteriaDialog) {
        AlertDialog(
            onDismissRequest = { settingsViewModel.showCriteriaDialog = false },
            title = { Text("Attendance Criteria (%)") },
            text = {
                OutlinedTextField(
                    value = settingsViewModel.newCriteria,
                    onValueChange = { settingsViewModel.newCriteria = it },
                    label = { Text("Enter Percentage") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = { settingsViewModel.updateCriteria() }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { settingsViewModel.showCriteriaDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
