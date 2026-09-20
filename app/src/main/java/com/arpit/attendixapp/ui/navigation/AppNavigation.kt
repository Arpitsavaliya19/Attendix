package com.arpit.attendixapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arpit.attendixapp.ui.screens.AdminMainScreen
import com.arpit.attendixapp.ui.screens.LoginScreen
import com.arpit.attendixapp.ui.screens.SignUpScreen
import com.arpit.attendixapp.ui.screens.StudentMainScreen
import com.arpit.attendixapp.util.SessionManager

/* ---------------- Navigation Routes ---------------- */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Filled.AccountCircle)
    object SignUp : Screen("signup", "Sign Up", Icons.Filled.Person)
    object AdminSignUp : Screen("admin_signup", "Admin Sign Up", Icons.Filled.VerifiedUser)
    object StudentMain : Screen("student_main", "Student Main", Icons.Filled.Home)
    object AdminMain : Screen("admin_main", "Admin Main", Icons.Filled.Home)

    object StudentDashboard : Screen("student_dashboard", "Profile", Icons.Filled.AccountCircle)
    object AdminDashboard : Screen("admin_dashboard", "Admin", Icons.Filled.VerifiedUser)
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
    object AttendanceReport : Screen("attendance_report", "Attendance Report", Icons.Filled.Assessment)
    object Subjects : Screen("subjects", "Subjects", Icons.Filled.Book)
    object Labs : Screen("labs", "Labs", Icons.Filled.School)
    object PracticalAttendanceReport : Screen("practical_attendance_report", "Practical Attendance", Icons.Filled.Assessment)
    object Schedules : Screen("schedules", "Schedules", Icons.Filled.CalendarToday)
    object ManageStudents : Screen("manage_students", "Manage Students", Icons.Filled.People)
    object ViewReports : Screen("view_reports", "View Reports", Icons.Filled.Assessment)
    object Notice : Screen("notice", "Notice", Icons.Filled.Notifications)
    object ManageStaff : Screen("manage_staff", "Manage Staff", Icons.Filled.People)
    object MarkAttendance : Screen("mark_attendance", "Mark Attendance", Icons.Filled.Assessment)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}

val studentBottomNavItems = listOf(Screen.Home, Screen.StudentDashboard, Screen.Settings)
val adminBottomNavItems = listOf(Screen.Home, Screen.AdminDashboard, Screen.Settings)


/* ---------------- App Navigation ---------------- */
@Composable
fun AttendixNavGraph(navController: NavHostController) {
    val startDestination = if (SessionManager.loggedInUserEmail != null) {
        if (SessionManager.loggedInUserRole == "Student") {
            Screen.StudentMain.route
        } else {
            Screen.AdminMain.route
        }
    } else {
        Screen.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { role: String ->
                    if (role == "Student") {
                        navController.navigate(Screen.StudentMain.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.AdminMain.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onSignupClick = { navController.navigate(Screen.SignUp.route) },
                onAdminSignupClick = { navController.navigate(Screen.AdminSignUp.route) },
                onGoogleLoginClick = { /* TODO Handle Google Login Click */ }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(initialRole = "Student", onCreateAccountClick = { role ->
                if (role == "Student") {
                    navController.navigate(Screen.StudentMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.AdminMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            })
        }

        composable(Screen.AdminSignUp.route) {
            SignUpScreen(initialRole = "Admin", onCreateAccountClick = { role ->
                if (role == "Student") {
                    navController.navigate(Screen.StudentMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.AdminMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            })
        }

        composable(Screen.StudentMain.route) {
            StudentMainScreen(rootNavController = navController)
        }
        composable(Screen.AdminMain.route) {
            AdminMainScreen(rootNavController = navController)
        }
    }
}
