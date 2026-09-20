package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.viewmodels.ViewReportsViewModel

class ViewReportsViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewReportsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewReportsViewModel(application.studentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun ViewReportsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val viewReportsViewModel: ViewReportsViewModel = viewModel(
        factory = ViewReportsViewModelFactory(application)
    )

    val students by viewReportsViewModel.students.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Select a Student to View Report",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(students) { student ->
            StudentReportItem(student = student) {
                // Navigate to the attendance report for the specific student
                navController.navigate("${Screen.AttendanceReport.route}/${student.email}")
            }
        }
    }
}

@Composable
fun StudentReportItem(student: Student, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(student.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text("Enrollment: ${student.enrollmentNumber}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewReportsScreenPreview() {
    ViewReportsScreen(rememberNavController())
}
