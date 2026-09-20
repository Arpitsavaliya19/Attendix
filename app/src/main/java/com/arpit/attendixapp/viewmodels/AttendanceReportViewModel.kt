package com.arpit.attendixapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.AttendanceStatus
import com.arpit.attendixapp.model.DayStatus
import com.arpit.attendixapp.model.Subject
import com.arpit.attendixapp.repository.SubjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random

class AttendanceReportViewModel(private val subjectRepository: SubjectRepository? = null) : ViewModel() {
    var selectedTabIndex by mutableIntStateOf(0)
    var tabs = mutableListOf("Weekly", "Monthly")

    private val _studentSubjects = MutableStateFlow<List<Subject>>(emptyList())
    val studentSubjects = _studentSubjects.asStateFlow()

    fun loadStudentSubjects(email: String) {
        if (subjectRepository == null) return
        viewModelScope.launch {
            subjectRepository.getSubjectsByStudent(email).collect { subjects ->
                _studentSubjects.value = subjects
            }
        }
    }

    fun onTabSelected(index: Int) {
        selectedTabIndex = index
    }

    var currentMonth by mutableStateOf(Calendar.getInstance())

    fun onMonthChange(newMonth: Calendar) {
        currentMonth = newMonth
    }

    fun getWeeklyAttendanceData(): List<DayStatus> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val days = (0..6).map {
            val dayCal = calendar.clone() as Calendar
            dayCal.add(Calendar.DAY_OF_YEAR, it)
            dayCal
        }

        return days.map {
            val dayOfWeek = it.get(Calendar.DAY_OF_WEEK)
            val status = when (dayOfWeek) {
                Calendar.SUNDAY, Calendar.SATURDAY -> AttendanceStatus.HOLIDAY
                else -> if (Random().nextFloat() > 0.3) AttendanceStatus.PRESENT else AttendanceStatus.ABSENT
            }
            DayStatus(it.get(Calendar.DAY_OF_MONTH), status)
        }
    }

    fun getMonthlyAttendanceData(month: Calendar): List<DayStatus> {
        val cal = month.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        return (1..daysInMonth).map {
            val dayCal = month.clone() as Calendar
            dayCal.set(Calendar.DAY_OF_MONTH, it)
            val status = when (dayCal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY, Calendar.SATURDAY -> AttendanceStatus.HOLIDAY
                else -> if (Random().nextFloat() > 0.3) AttendanceStatus.PRESENT else AttendanceStatus.ABSENT
            }
            DayStatus(it, status)
        }
    }
}
