package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.attendixapp.viewmodels.SchedulesViewModel

@Composable
fun SchedulesScreen(schedulesViewModel: SchedulesViewModel = viewModel()) {
    val schedule = remember(schedulesViewModel.selectedDayIndex) {
        schedulesViewModel.getScheduleForDay(schedulesViewModel.selectedDayIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Day Selector Tabs
        TabRow(selectedTabIndex = schedulesViewModel.selectedDayIndex) {
            schedulesViewModel.weekDays.forEachIndexed { index, day ->
                Tab(
                    selected = schedulesViewModel.selectedDayIndex == index,
                    onClick = { schedulesViewModel.onDaySelected(index) },
                    text = { Text(day) }
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${schedulesViewModel.weekDays[schedulesViewModel.selectedDayIndex]}'s Schedule",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Timetable
            Column(modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))) {
                ScheduleHeader()
                schedule.forEach { (time, subject) ->
                    ScheduleRow(time = time, subject = subject)
                }
            }
        }
    }
}

@Composable
fun ScheduleHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(
            text = "Time",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Subject / Lab",
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
    HorizontalDivider()
}

@Composable
fun ScheduleRow(time: String, subject: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = time,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = subject,
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (subject == "Lunch Break") FontWeight.Bold else FontWeight.Normal
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun SchedulesScreenPreview() {
    SchedulesScreen()
}
