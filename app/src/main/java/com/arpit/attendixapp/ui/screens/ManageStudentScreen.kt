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
import com.arpit.attendixapp.viewmodels.ManageStudentsViewModel

class ManageStudentsViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageStudentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageStudentsViewModel(application.studentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun ManageStudentsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val manageStudentsViewModel: ManageStudentsViewModel = viewModel(
        factory = ManageStudentsViewModelFactory(application)
    )

    val students by manageStudentsViewModel.students.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { manageStudentsViewModel.onAddStudentClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Student")
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
                "Student Directory",
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
                if (students.isEmpty()) {
                    item {
                        Text(
                            "No students added yet. Click the + button to add a new student.",
                            modifier = Modifier.padding(vertical = 24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(students) { student ->
                    StudentListItem(student, onDelete = { manageStudentsViewModel.deleteStudent(student) })
                }
            }
        }

        if (manageStudentsViewModel.showAddStudentDialog) {
            AddStudentDialog(
                onDismiss = { manageStudentsViewModel.onDismissAddStudentDialog() },
                onAdd = { manageStudentsViewModel.onConfirmAddStudent() },
                viewModel = manageStudentsViewModel
            )
        }
    }
}

@Composable
fun StudentListItem(student: Student, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
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
                Text(student.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Enrollment: ${student.enrollmentNumber}", style = MaterialTheme.typography.bodyMedium)
                Text("Email: ${student.email}", style = MaterialTheme.typography.bodyMedium)
                Text("Semester: ${student.semester}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Student",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit, onAdd: () -> Unit,
    viewModel: ManageStudentsViewModel
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Student") },
        text = {
            Column {
                OutlinedTextField(
                    value = viewModel.newStudentName,
                    onValueChange = { viewModel.newStudentName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.newStudentEnrollment,
                    onValueChange = { viewModel.newStudentEnrollment = it },
                    label = { Text("Enrollment No.") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.newStudentEmail,
                    onValueChange = { viewModel.newStudentEmail = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.newStudentSemester,
                    onValueChange = { viewModel.newStudentSemester = it },
                    label = { Text("Current Semester") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onAdd) {
                Text("Save Student")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
