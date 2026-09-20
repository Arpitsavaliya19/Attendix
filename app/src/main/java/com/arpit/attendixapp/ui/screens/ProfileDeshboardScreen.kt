package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.*

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/* ---------------- Main Screens ---------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(rootNavController: NavHostController) {
    val innerNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(
                role = "Student",
                onProfileClick = { innerNavController.navigate(Screen.Profile.route) }
            )
        },
        bottomBar = {
            BottomNavBar(navController = innerNavController, items = studentBottomNavItems, rootNavController = rootNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.StudentDashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.StudentDashboard.route) { StudentDashboardScreen(innerNavController) }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.AttendanceReport.route) { AttendanceReportScreen() }
            composable(Screen.Subjects.route) { SubjectsScreen(innerNavController) }
            composable(Screen.Labs.route) { LabsScreen(innerNavController) }
            composable(Screen.PracticalAttendanceReport.route) { PracticalAttendanceReportScreen() }
            composable(Screen.Schedules.route) { SchedulesScreen() }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = {
                    rootNavController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(rootNavController: NavHostController) {
    val innerNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(
                role = "Admin",
                onProfileClick = { innerNavController.navigate(Screen.Profile.route) }
            )
        },
        bottomBar = {
            BottomNavBar(navController = innerNavController, items = adminBottomNavItems, rootNavController = rootNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.AdminDashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.AdminDashboard.route) { AdminDashboardScreen(innerNavController) }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.ManageStudents.route) { ManageStudentsScreen() }
            composable(Screen.ViewReports.route) { ViewReportsScreen(innerNavController) }
            composable(Screen.Notice.route) { NoticeScreen() }
            composable(Screen.ManageStaff.route) { ManageStaffScreen() }
            composable(Screen.MarkAttendance.route) { MarkAttendanceScreen(innerNavController) }
            composable(
                route = "${Screen.AttendanceReport.route}/{studentEmail}",
                arguments = listOf(navArgument("studentEmail") { type = NavType.StringType })
            ) { backStackEntry ->
                AttendanceReportScreen(studentEmail = backStackEntry.arguments?.getString("studentEmail"))
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = {
                    rootNavController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
        }
    }
}


/* ---------------- Previews ---------------- */
@Preview(showBackground = true)
@Composable
fun StudentMainScreenPreview() {
    StudentMainScreen(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun AdminMainScreenPreview() {
    AdminMainScreen(rememberNavController())
}
