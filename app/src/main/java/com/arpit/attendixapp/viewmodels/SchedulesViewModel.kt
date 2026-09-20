package com.arpit.attendixapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SchedulesViewModel : ViewModel() {
    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    var selectedDayIndex by mutableIntStateOf(0)

    fun onDaySelected(index: Int) {
        selectedDayIndex = index
    }

    fun getScheduleForDay(dayIndex: Int): List<Pair<String, String>> {
        val classes = (listOf(
            "Data Structures",
            "Operating Systems",
            "DBMS",
            "Computer Networks",
            "Software Engineering",
        ) + listOf(
            "Data Structures Lab",
            "OS Lab",
            "DBMS Lab",
            "Networks Lab",
        ) + listOf("Break")).shuffled(java.util.Random(dayIndex.toLong()))

        val timeSlots = listOf(
            "09:00 - 10:00 AM",
            "10:00 - 11:00 AM",
            "11:00 - 12:00 PM",
            "12:00 - 01:00 PM", // Lunch
            "02:00 - 03:00 PM",
            "03:00 - 04:00 PM"
        )

        val classIterator = classes.iterator()
        return timeSlots.mapIndexed { index, time ->
            val subject = if (index == 3) "Lunch Break" else if (classIterator.hasNext()) classIterator.next() else "Free Period"
            time to subject
        }
    }
}
