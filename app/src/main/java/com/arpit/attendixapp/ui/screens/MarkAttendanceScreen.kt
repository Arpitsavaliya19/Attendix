package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.arpit.attendixapp.viewmodels.MarkAttendanceViewModel
import com.arpit.attendixapp.model.Student
import com.arpit.attendixapp.model.Lecture
import com.arpit.attendixapp.model.AttendanceStatus

class MarkAttendanceViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarkAttendanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarkAttendanceViewModel(
                application.studentRepository,
                application.subjectRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun MarkAttendanceScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val markAttendanceViewModel: MarkAttendanceViewModel = viewModel(
        factory = MarkAttendanceViewModelFactory(application)
    )

    val students by markAttendanceViewModel.students.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (markAttendanceViewModel.selectedStudent == null) {
            Text("Select a Student", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            if (students.isEmpty()) {
                Text(
                    "No students found. Please add students from 'Manage Students' first.",
                    modifier = Modifier.padding(vertical = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(students) { student ->
                        StudentToMarkItem(student = student) { markAttendanceViewModel.onStudentSelected(student) }
                    }
                }
            }
        } else {
            StudentAttendanceSheet(
                student = markAttendanceViewModel.selectedStudent!!,
                onBack = { markAttendanceViewModel.onBackToStudentSelection() },
                viewModel = markAttendanceViewModel
            )
        }
    }
}

@Composable
fun StudentToMarkItem(student: Student, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Text(student.name, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun StudentAttendanceSheet(student: Student, onBack: () -> Unit, viewModel: MarkAttendanceViewModel) {
    val studentSubjects by viewModel.studentSubjects.collectAsState()

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(student.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            TextButton(onClick = onBack) { Text("Change Student") }
        }
        Text("Mark Today's Attendance", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        if (studentSubjects.isEmpty()) {
            Text(
                "This student has not added any subjects yet.",
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(studentSubjects) { lecture ->
                    LectureAttendanceItem(
                        lecture = lecture,
                        status = viewModel.attendanceStates[lecture.name],
                        onStatusChange = { newStatus ->
                            viewModel.onStatusChange(lecture, newStatus)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LectureAttendanceItem(
    lecture: Lecture,
    status: AttendanceStatus?,
    onStatusChange: (AttendanceStatus) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(lecture.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ElevatedButton(
                    onClick = { onStatusChange(AttendanceStatus.PRESENT) },
                    modifier = Modifier.weight(1f),
                    colors = if (status == AttendanceStatus.PRESENT) ButtonDefaults.buttonColors(containerColor = AttendanceStatus.PRESENT.color, contentColor = Color.White) else ButtonDefaults.elevatedButtonColors()
                ) {
                    Text("Present")
                }
                ElevatedButton(
                    onClick = { onStatusChange(AttendanceStatus.ABSENT) },
                    modifier = Modifier.weight(1f),
                    colors = if (status == AttendanceStatus.ABSENT) ButtonDefaults.buttonColors(containerColor = AttendanceStatus.ABSENT.color, contentColor = Color.White) else ButtonDefaults.elevatedButtonColors()
                ) {
                    Text("Absent")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarkAttendanceScreenPreview() {
    MarkAttendanceScreen(rememberNavController())
}
