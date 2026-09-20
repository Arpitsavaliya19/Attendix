package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.attendixapp.viewmodels.AttendanceReportViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arpit.attendixapp.AttendixApplication

class AttendanceReportViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AttendanceReportViewModel(application.subjectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun AttendanceReportScreen(studentEmail: String? = null) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val attendanceReportViewModel: AttendanceReportViewModel = viewModel(
        factory = AttendanceReportViewModelFactory(application)
    )

    LaunchedEffect(studentEmail) {
        if (studentEmail != null) {
            attendanceReportViewModel.loadStudentSubjects(studentEmail)
            if (!attendanceReportViewModel.tabs.contains("Subjects")) {
                attendanceReportViewModel.tabs.add("Subjects")
            }
            attendanceReportViewModel.onTabSelected(2) // Select Subjects tab by default for admin view
        }
    }

    val studentSubjects by attendanceReportViewModel.studentSubjects.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (studentEmail != null) {
            Text(
                text = "Report for $studentEmail",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
        TabRow(selectedTabIndex = attendanceReportViewModel.selectedTabIndex) {
            attendanceReportViewModel.tabs.forEachIndexed { index, title ->
                Tab(
                    selected = attendanceReportViewModel.selectedTabIndex == index,
                    onClick = { attendanceReportViewModel.onTabSelected(index) }
                ) {
                    Text(title, modifier = Modifier.padding(12.dp))
                }
            }
        }
        when (attendanceReportViewModel.selectedTabIndex) {
            0 -> WeeklyAttendanceView(attendanceReportViewModel)
            1 -> MonthlyAttendanceView(attendanceReportViewModel)
            2 -> {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    lazyItems(studentSubjects) { subject ->
                        MySubjectItem(
                            subject = subject,
                            onTrackAttendance = {},
                            onRemove = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyAttendanceView(viewModel: AttendanceReportViewModel) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    val days = (0..6).map {
        val dayCal = calendar.clone() as Calendar
        dayCal.add(Calendar.DAY_OF_YEAR, it)
        dayCal
    }

    val attendanceData = remember { viewModel.getWeeklyAttendanceData() }

    val totalClasses =
        attendanceData.count { it.status == AttendanceStatus.PRESENT || it.status == AttendanceStatus.ABSENT }
    val attendedClasses = attendanceData.count { it.status == AttendanceStatus.PRESENT }
    val missedClasses = attendanceData.count { it.status == AttendanceStatus.ABSENT }
    val attendancePercentage =
        if (totalClasses > 0) (attendedClasses.toFloat() / totalClasses * 100) else 0f
    val attendanceCriteria = com.arpit.attendixapp.util.SessionManager.attendanceCriteria
    val isCriteriaMet = attendancePercentage >= attendanceCriteria

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Week Calendar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            days.forEachIndexed { index, day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(SimpleDateFormat("E", Locale.US).format(day.time))
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(attendanceData[index].status.color),
                        contentAlignment = Alignment.Center
                    ) {
                        if (attendanceData[index].status != AttendanceStatus.NONE) {
                            Text(attendanceData[index].day.toString(), color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Attendance Summary Card
        Text(
            "Weekly Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryRow("Total Classes:", "$totalClasses")
                SummaryRow("Attended:", "$attendedClasses")
                SummaryRow("Missed:", "$missedClasses")
                SummaryRow("Percentage:", "%.1f%%".format(attendancePercentage))
                SummaryRow(
                    label = "Criteria (>=${attendanceCriteria.toInt()}%):",
                    value = if (isCriteriaMet) "Met" else "Not Met",
                    valueColor = if (isCriteriaMet) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun MonthlyAttendanceView(viewModel: AttendanceReportViewModel) {
    val cal = viewModel.currentMonth.clone() as Calendar
    cal.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    val monthOffset = (firstDayOfWeek - cal.firstDayOfWeek + 7) % 7
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val attendanceData = remember(viewModel.currentMonth) {
        viewModel.getMonthlyAttendanceData(viewModel.currentMonth)
    }

    val totalClasses =
        attendanceData.count { it.status == AttendanceStatus.PRESENT || it.status == AttendanceStatus.ABSENT }
    val attendedClasses = attendanceData.count { it.status == AttendanceStatus.PRESENT }
    val missedClasses = attendanceData.count { it.status == AttendanceStatus.ABSENT }
    val attendancePercentage =
        if (totalClasses > 0) (attendedClasses.toFloat() / totalClasses * 100) else 0f
    val attendanceCriteria = com.arpit.attendixapp.util.SessionManager.attendanceCriteria
    val isCriteriaMet = attendancePercentage >= attendanceCriteria

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .padding(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            MonthNavigator(currentMonth = viewModel.currentMonth, onMonthChange = { viewModel.onMonthChange(it) })
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(16.dp))
        }

        items(weekDays) {
            Text(it, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }

        items(count = monthOffset) {
            Box(Modifier.size(40.dp))
        }

        items(attendanceData) { dayStatus ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(dayStatus.status.color),
                contentAlignment = Alignment.Center
            ) {
                if (dayStatus.status != AttendanceStatus.NONE) {
                    Text(dayStatus.day.toString(), color = Color.White)
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    "Monthly Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SummaryRow("Total Classes:", "$totalClasses")
                        SummaryRow("Attended:", "$attendedClasses")
                        SummaryRow("Missed:", "$missedClasses")
                        SummaryRow("Percentage:", "%.1f%%".format(attendancePercentage))
                        SummaryRow(
                            label = "Criteria (>=75%):",
                            value = if (isCriteriaMet) "Met" else "Not Met",
                            valueColor = if (isCriteriaMet) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonthNavigator(currentMonth: Calendar, onMonthChange: (Calendar) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val prevMonth = currentMonth.clone() as Calendar
            prevMonth.add(Calendar.MONTH, -1)
            onMonthChange(prevMonth)
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Text(
            text = SimpleDateFormat("MMMM yyyy", Locale.US).format(currentMonth.time),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = {
            val nextMonth = currentMonth.clone() as Calendar
            nextMonth.add(Calendar.MONTH, 1)
            onMonthChange(nextMonth)
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttendanceReportScreenPreview() {
    AttendanceReportScreen()
}
