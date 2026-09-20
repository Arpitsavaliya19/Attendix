package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.data.local.entity.StaffEntity
import com.arpit.attendixapp.data.local.entity.StudentEntity
import com.arpit.attendixapp.data.local.entity.SubjectEntity
import com.arpit.attendixapp.data.local.entity.UserEntity
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val userDao = application.database.userDao()
    val subjectDao = application.database.subjectDao()
    val studentDao = application.database.studentDao()
    val staffDao = application.database.staffDao()
    
    var user by remember { mutableStateOf<UserEntity?>(null) }
    val subjects = remember { mutableStateListOf<SubjectEntity>() }
    val students = remember { mutableStateListOf<StudentEntity>() }
    val staffList = remember { mutableStateListOf<StaffEntity>() }
    
    val loggedInEmail = SessionManager.loggedInUserEmail

    LaunchedEffect(loggedInEmail) {
        if (loggedInEmail != null) {
            withContext(Dispatchers.IO) {
                user = userDao.getUserByEmail(loggedInEmail)
                if (user?.role == "Student") {
                    val dbSubjects = subjectDao.getSubjectsByStudentStatic(loggedInEmail)
                    withContext(Dispatchers.Main) {
                        subjects.clear()
                        subjects.addAll(dbSubjects)
                    }
                } else if (user?.role == "Admin") {
                    val dbStudents = studentDao.getStudentsByAdminStatic(loggedInEmail)
                    val dbStaff = staffDao.getStaffByAdminStatic(loggedInEmail)
                    withContext(Dispatchers.Main) {
                        students.clear()
                        students.addAll(dbStudents)
                        staffList.clear()
                        staffList.addAll(dbStaff)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Profile Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user?.username ?: "User Name",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = user?.role ?: "Role",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileDetailRow(label = "Email", value = user?.email ?: "N/A", icon = Icons.Default.Email)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                ProfileDetailRow(label = "Password", value = user?.password?.let { "*".repeat(it.length) } ?: "N/A", icon = Icons.Default.Lock)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                ProfileDetailRow(label = "Role", value = user?.role ?: "N/A", icon = Icons.Default.VerifiedUser)
            }
        }

        if (user?.role == "Student" && subjects.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Enrolled Subjects",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    subjects.forEachIndexed { index, subject ->
                        ProfileDetailRow(label = "Subject", value = subject.name, icon = Icons.Default.Book)
                        if (index < subjects.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }

        if (user?.role == "Admin") {
            if (students.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Managed Students",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        students.take(3).forEachIndexed { index, student ->
                            ProfileDetailRow(label = "Student", value = "${student.name} (${student.enrollmentNumber})", icon = Icons.Default.Badge)
                            if (index < students.take(3).size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                        if (students.size > 3) {
                            Text(
                                text = "and ${students.size - 3} more...",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            if (staffList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Managed Staff",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        staffList.take(3).forEachIndexed { index, staff ->
                            ProfileDetailRow(label = "Staff", value = "${staff.name} (${staff.employeeId})", icon = Icons.Default.Badge)
                            if (index < staffList.take(3).size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                        if (staffList.size > 3) {
                            Text(
                                text = "and ${staffList.size - 3} more...",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                SessionManager.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Logout")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}
