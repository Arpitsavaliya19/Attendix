package com.arpit.attendixapp.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.attendixapp.repository.UserRepository
import com.arpit.attendixapp.util.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var selectedRole by mutableStateOf("Student")
    
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var loginError by mutableStateOf<String?>(null)

    // Forgot Password State
    var showForgotPasswordDialog by mutableStateOf(false)
    var forgotEmail by mutableStateOf("")
    var forgotNewPassword by mutableStateOf("")
    var forgotEmailError by mutableStateOf<String?>(null)
    var forgotNewPasswordError by mutableStateOf<String?>(null)
    var resetSuccessMessage by mutableStateOf<String?>(null)

    fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = null
        loginError = null
        resetSuccessMessage = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        passwordError = null
        loginError = null
    }

    fun onRoleChange(newRole: String) {
        selectedRole = newRole
    }

    fun onForgotPasswordClick() {
        forgotEmail = email
        forgotNewPassword = ""
        forgotEmailError = null
        forgotNewPasswordError = null
        showForgotPasswordDialog = true
    }

    fun onDismissForgotPasswordDialog() {
        showForgotPasswordDialog = false
    }

    fun onConfirmResetPassword() {
        var isValid = true
        if (forgotEmail.isBlank()) {
            forgotEmailError = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(forgotEmail).matches()) {
            forgotEmailError = "Invalid email format"
            isValid = false
        } else {
            forgotEmailError = null
        }

        if (forgotNewPassword.isBlank()) {
            forgotNewPasswordError = "New password cannot be empty"
            isValid = false
        } else if (forgotNewPassword.length < 6) {
            forgotNewPasswordError = "Password must be at least 6 characters"
            isValid = false
        } else {
            forgotNewPasswordError = null
        }

        if (isValid) {
            viewModelScope.launch {
                val user = userRepository.getUserByEmail(forgotEmail)
                if (user == null) {
                    forgotEmailError = "No account found with this email"
                } else {
                    val success = userRepository.updatePasswordByEmail(forgotEmail, forgotNewPassword)
                    if (success) {
                        resetSuccessMessage = "Password reset successfully! You can now log in."
                        showForgotPasswordDialog = false
                    } else {
                        forgotEmailError = "Failed to update password. Try again."
                    }
                }
            }
        }
    }

    fun performLogin(onSuccess: (role: String) -> Unit) {
        if (validateInputs()) {
            viewModelScope.launch {
                val user = userRepository.getUserByEmail(email)
                if (user == null) {
                    loginError = "No account found with this email"
                } else if (user.password != password) {
                    passwordError = "Incorrect password"
                } else if (user.role != selectedRole) {
                    loginError = "This account is not registered as a ${selectedRole}"
                } else {
                    SessionManager.login(user.email, user.role, user.username)
                    onSuccess(user.role)
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        
        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else {
            passwordError = null
        }

        return isValid
    }
}
