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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun PracticalAttendanceReportScreen(attendanceReportViewModel: AttendanceReportViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
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
            0 -> PracticalWeeklyAttendanceView(attendanceReportViewModel)
            1 -> PracticalMonthlyAttendanceView(attendanceReportViewModel)
        }
    }
}

@Composable
fun PracticalWeeklyAttendanceView(viewModel: AttendanceReportViewModel) {
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
    val attendanceCriteria = 75.0
    val isCriteriaMet = attendancePercentage >= attendanceCriteria

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    label = "Criteria (>=75%):",
                    value = if (isCriteriaMet) "Met" else "Not Met",
                    valueColor = if (isCriteriaMet) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun PracticalMonthlyAttendanceView(viewModel: AttendanceReportViewModel) {
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
    val attendanceCriteria = 75.0
    val isCriteriaMet = attendancePercentage >= attendanceCriteria

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxSize()
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

@Preview(showBackground = true)
@Composable
fun PracticalAttendanceReportScreenPreview() {
    PracticalAttendanceReportScreen()
}
