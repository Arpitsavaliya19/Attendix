package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.viewmodels.ManageStaffViewModel

class ManageStaffViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageStaffViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageStaffViewModel(application.staffRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun ManageStaffScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val manageStaffViewModel: ManageStaffViewModel = viewModel(
        factory = ManageStaffViewModelFactory(application)
    )

    val staffList by manageStaffViewModel.staffList.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { manageStaffViewModel.onAddStaffClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Staff")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Staff Directory",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (staffList.isEmpty()) {
                    item {
                        Text(
                            "No staff members added yet. Click the + button to add a new staff member.",
                            modifier = Modifier.padding(vertical = 24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(staffList) { staff ->
                    StaffListItem(staff, onDelete = { manageStaffViewModel.deleteStaff(staff) })
                }
            }
        }

        if (manageStaffViewModel.showAddStaffDialog) {
            AddStaffDialog(
                onDismiss = { manageStaffViewModel.onDismissAddStaffDialog() },
                onAdd = { manageStaffViewModel.onConfirmAddStaff() },
                viewModel = manageStaffViewModel
            )
        }
    }
}

@Composable
fun StaffListItem(staff: Staff, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(staff.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("ID: ${staff.employeeId}", style = MaterialTheme.typography.bodyMedium)
                Text("Department: ${staff.department}", style = MaterialTheme.typography.bodyMedium)
                Text("Subject: ${staff.subject}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Staff",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddStaffDialog(
    onDismiss: () -> Unit, onAdd: () -> Unit,
    viewModel: ManageStaffViewModel
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Staff Member") },
        text = {
            Column {
                OutlinedTextField(
                    value = viewModel.newStaffName,
                    onValueChange = { viewModel.newStaffName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.newStaffEmployeeId,
                    onValueChange = { viewModel.newStaffEmployeeId = it },
                    label = { Text("Employee ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.newStaffDepartment,
                    onValueChange = { viewModel.newStaffDepartment = it },
                    label = { Text("Department") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.newStaffSubject,
                    onValueChange = { viewModel.newStaffSubject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onAdd) {
                Text("Save Staff")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
