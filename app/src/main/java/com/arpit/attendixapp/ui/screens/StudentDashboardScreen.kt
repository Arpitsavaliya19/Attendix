package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun StudentDashboardScreen(navController: NavHostController) {
    val dashboardItems = remember {
        listOf(
            DashboardItem("Subjects", Icons.Filled.Book),
            DashboardItem("Labs", Icons.Filled.School),
            DashboardItem("Attendance", Icons.Filled.Assessment),
            DashboardItem("Schedules", Icons.Filled.CalendarToday)
        )
    }
    DashboardGrid(items = dashboardItems) { item ->
        when (item.title) {
            "Subjects" -> navController.navigate(Screen.Subjects.route)
            "Labs" -> navController.navigate(Screen.Labs.route)
            "Attendance" -> navController.navigate(Screen.AttendanceReport.route)
            "Schedules" -> navController.navigate(Screen.Schedules.route)
        }
    }
}
