package com.arpit.attendixapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Persistent session manager to track the logged-in user using SharedPreferences.
 */
object SessionManager {
    private const val PREF_NAME = "attendix_session"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_ROLE = "user_role"
    private const val KEY_USERNAME = "user_username"
    private const val KEY_CRITERIA = "attendance_criteria"

    private lateinit var prefs: SharedPreferences

    var loggedInUserEmail by mutableStateOf<String?>(null)
    var loggedInUserRole by mutableStateOf<String?>(null)
    var loggedInUserName by mutableStateOf<String?>(null)
    var attendanceCriteria by mutableStateOf(75f)

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loggedInUserEmail = prefs.getString(KEY_EMAIL, null)
        loggedInUserRole = prefs.getString(KEY_ROLE, null)
        loggedInUserName = prefs.getString(KEY_USERNAME, null)
        attendanceCriteria = prefs.getFloat(KEY_CRITERIA, 75f)
    }

    fun login(email: String, role: String, username: String) {
        loggedInUserEmail = email
        loggedInUserRole = role
        loggedInUserName = username
        
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_ROLE, role)
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    fun logout() {
        loggedInUserEmail = null
        loggedInUserRole = null
        loggedInUserName = null
        
        prefs.edit().clear().apply()
    }

    fun updateCriteria(newCriteria: Float) {
        attendanceCriteria = newCriteria
        prefs.edit().putFloat(KEY_CRITERIA, newCriteria).apply()
    }
}
