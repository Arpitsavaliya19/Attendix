package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AdminDashboardScreen(navController: NavHostController) {
    val dashboardItems = remember {
        listOf(
            DashboardItem("Manage Students", Icons.Filled.People),
            DashboardItem("Manage Staff", Icons.Filled.School),
            DashboardItem("View Reports", Icons.Filled.Assessment),
            DashboardItem("Notice", Icons.Filled.Notifications),
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dashboardItems) { item ->
            DashboardCard(item = item, onClick = {
                when (item.title) {
                    "Manage Students" -> navController.navigate(Screen.ManageStudents.route)
                    "View Reports" -> navController.navigate(Screen.ViewReports.route)
                    "Notice" -> navController.navigate(Screen.Notice.route)
                    "Manage Staff" -> navController.navigate(Screen.ManageStaff.route)
                }
            })
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            DashboardCard(
                item = DashboardItem("Mark Attendance", Icons.Filled.Assessment),
                onClick = { navController.navigate(Screen.MarkAttendance.route) }
            )
        }
    }
}
