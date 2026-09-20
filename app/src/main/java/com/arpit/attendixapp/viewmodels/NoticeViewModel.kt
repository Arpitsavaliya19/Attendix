package com.arpit.attendixapp.viewmodels

import androidx.lifecycle.ViewModel
import com.arpit.attendixapp.model.Notice

class NoticeViewModel : ViewModel() {
    val notices = listOf(
        Notice(
            title = "Lecture Rescheduled",
            content = "The lecture for 'Data Structures & Algorithms' scheduled for 10:00 AM on Monday has been rescheduled to 02:00 PM on the same day.",
            date = "2024-07-29"
        ),
        Notice(
            title = "Faculty Lecture Postponed",
            content = "Dr. Smith's lecture on 'Operating Systems' for Tuesday has been postponed. A new date will be announced shortly.",
            date = "2024-07-28"
        ),
        Notice(
            title = "Extra Lab Session",
            content = "There will be an extra lab session for 'Computer Networks' this Friday at 03:00 PM.",
            date = "2024-07-27"
        ),
        Notice(
            title = "Important: Project Submission",
            content = "The final project submission deadline for 'Software Engineering' has been extended to August 5th.",
            date = "2024-07-26"
        )
    )
}
