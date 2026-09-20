package com.arpit.attendixapp

import android.app.Application
import com.arpit.attendixapp.data.local.database.AppDatabase
import com.arpit.attendixapp.data.repository.DefaultLabRepository
import com.arpit.attendixapp.data.repository.DefaultStaffRepository
import com.arpit.attendixapp.data.repository.DefaultStudentRepository
import com.arpit.attendixapp.data.repository.DefaultSubjectRepository
import com.arpit.attendixapp.data.repository.DefaultUserRepository
import com.arpit.attendixapp.repository.LabRepository
import com.arpit.attendixapp.repository.StaffRepository
import com.arpit.attendixapp.repository.StudentRepository
import com.arpit.attendixapp.repository.SubjectRepository
import com.arpit.attendixapp.repository.UserRepository
import com.arpit.attendixapp.util.NotificationHelper
import com.arpit.attendixapp.util.SessionManager

class AttendixApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    val userRepository: UserRepository by lazy { DefaultUserRepository(database.userDao()) }
    val subjectRepository: SubjectRepository by lazy { DefaultSubjectRepository(database.subjectDao()) }
    val labRepository: LabRepository by lazy { DefaultLabRepository(database.labDao()) }
    val studentRepository: StudentRepository by lazy { DefaultStudentRepository(database.studentDao()) }
    val staffRepository: StaffRepository by lazy { DefaultStaffRepository(database.staffDao()) }

    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
        NotificationHelper.createNotificationChannel(this)
    }
}
