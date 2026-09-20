package com.arpit.attendixapp.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.model.User
import com.arpit.attendixapp.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var selectedRole by mutableStateOf("Student")

    var usernameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    fun onUsernameChange(newUsername: String) {
        username = newUsername
        usernameError = null
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        passwordError = null
    }
    
    fun onRoleChange(newRole: String) {
        selectedRole = newRole
    }

    fun validateAndSignUp(onSuccess: () -> Unit) {
        if (validate()) {
            viewModelScope.launch {
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    emailError = "User with this email already exists"
                } else {
                    userRepository.insertUser(
                        User(
                            email = email,
                            username = username,
                            password = password,
                            role = selectedRole
                        )
                    )
                    com.arpit.attendixapp.util.SessionManager.login(email, selectedRole, username)
                    onSuccess()
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (username.isBlank()) {
            usernameError = "Username cannot be empty"
            isValid = false
        }

        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        }

        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }
}
