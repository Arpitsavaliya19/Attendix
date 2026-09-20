package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.viewmodels.LabsViewModel
import kotlin.math.ceil

class LabsViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LabsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LabsViewModel(application.labRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun LabsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val labsViewModel: LabsViewModel = viewModel(
        factory = LabsViewModelFactory(application)
    )

    val myLabs by labsViewModel.myLabs.collectAsState()
    val availableLabs = labsViewModel.availableLabs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("My Labs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1.3f)) {
            if (myLabs.isEmpty()) {
                item {
                    Text(
                        "You haven't added any labs yet. Add from the list below.",
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
            items(myLabs) { lab ->
                MyLabItem(
                    lab = lab,
                    onTrackAttendance = {
                        navController.navigate(Screen.PracticalAttendanceReport.route)
                    },
                    onRemove = {
                        labsViewModel.removeLab(lab)
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        Text("Available Labs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(0.7f)) {
            items(availableLabs) { lab ->
                AvailableLabItem(lab = lab, onAdd = {
                    labsViewModel.addLab(lab)
                })
            }
            if (availableLabs.isEmpty()) {
                item {
                    Text(
                        "All available labs have been added.",
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MyLabItem(
    lab: Lab,
    onTrackAttendance: () -> Unit,
    onRemove: () -> Unit
) {
    val labColor = Color(android.graphics.Color.parseColor(lab.colorHex))
    val statusText = getLabStatusText(lab.attended, lab.total)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(labColor)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        lab.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, "Remove", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Attendance Percentage and Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${lab.percentage.toInt()}% Attendance",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (lab.percentage >= com.arpit.attendixapp.util.SessionManager.attendanceCriteria) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                Text(
                    text = "${lab.attended}/${lab.total} Practicals",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { lab.percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (lab.percentage >= com.arpit.attendixapp.util.SessionManager.attendanceCriteria) Color(0xFF4CAF50) else Color(0xFFF44336),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(16.dp))

            // Action Buttons
            Button(
                onClick = onTrackAttendance,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("View Detailed Report", fontSize = 14.sp)
            }
        }
    }
}

private fun getLabStatusText(attended: Int, total: Int): String {
    if (total == 0) return "Start tracking to see your status"
    val currentPercent = (attended.toFloat() / total.toFloat()) * 100f
    val criteria = com.arpit.attendixapp.util.SessionManager.attendanceCriteria
    val criteriaDecimal = criteria / 100f
    
    return if (currentPercent >= criteria) {
        val canMiss = (attended / criteriaDecimal).toInt() - total
        if (canMiss > 0) "You can miss the next $canMiss practicals."
        else "You are on the limit! Don't miss the next practical."
    } else {
        val needToAttend = ceil((criteriaDecimal * total - attended) / (1 - criteriaDecimal)).toInt()
        "Attend next $needToAttend practicals to reach ${criteria.toInt()}%."
    }
}

@Composable
fun AvailableLabItem(lab: Lab, onAdd: () -> Unit) {
    val labColor = Color(android.graphics.Color.parseColor(lab.colorHex))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onAdd),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(labColor))
                Spacer(Modifier.width(8.dp))
                Text(lab.name, style = MaterialTheme.typography.bodyLarge)
            }
            Icon(Icons.Default.Add, "Add Lab")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabsScreenPreview() {
    LabsScreen(rememberNavController())
}
