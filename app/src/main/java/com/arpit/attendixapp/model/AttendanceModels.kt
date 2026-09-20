package com.arpit.attendixapp.model

import androidx.compose.ui.graphics.Color

enum class AttendanceStatus(val color: Color) {
    PRESENT(Color(0xFF4CAF50)),
    ABSENT(Color(0xFFF44336)),
    HOLIDAY(Color.Gray),
    NONE(Color.Transparent)
}

data class DayStatus(val day: Int, val status: AttendanceStatus)
