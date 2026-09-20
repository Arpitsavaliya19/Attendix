package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.attendixapp.AttendixApplication
import com.arpit.attendixapp.R
import com.arpit.attendixapp.viewmodels.LoginViewModel

class LoginViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application.userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (role: String) -> Unit,
    onSignupClick: () -> Unit,
    onAdminSignupClick: () -> Unit,
    onGoogleLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(application)
    )

    val roles = listOf("Student", "Admin")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Attendix",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Smart Attendance Management",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                roles.forEachIndexed { index, role ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = roles.size),
                        onClick = { loginViewModel.onRoleChange(role) },
                        selected = loginViewModel.selectedRole == role
                    ) {
                        Text(role)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (loginViewModel.loginError != null) {
                Text(
                    text = loginViewModel.loginError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (loginViewModel.resetSuccessMessage != null) {
                Text(
                    text = loginViewModel.resetSuccessMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = loginViewModel.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Email") },
                singleLine = true,
                isError = loginViewModel.emailError != null,
                supportingText = {
                    loginViewModel.emailError?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                singleLine = true,
                isError = loginViewModel.passwordError != null,
                supportingText = {
                    loginViewModel.passwordError?.let { Text(it) }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { loginViewModel.onForgotPasswordClick() }
                ) {
                    Text("Forgot Password?")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { 
                    loginViewModel.performLogin(onSuccess = onLoginClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = CircleShape
            ) {
                Text(text = "Login as ${loginViewModel.selectedRole}", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = CircleShape
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Login with Google", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account?")
                    TextButton(onClick = onSignupClick) {
                        Text("Sign up")
                    }
                }
                TextButton(onClick = onAdminSignupClick) {
                    Text("Sign up as Admin", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }

    if (loginViewModel.showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { loginViewModel.onDismissForgotPasswordDialog() },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text(
                        text = "Enter your registered email address and new password.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = loginViewModel.forgotEmail,
                        onValueChange = { loginViewModel.forgotEmail = it; loginViewModel.forgotEmailError = null },
                        label = { Text("Email") },
                        singleLine = true,
                        isError = loginViewModel.forgotEmailError != null,
                        supportingText = {
                            loginViewModel.forgotEmailError?.let { Text(it) }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = loginViewModel.forgotNewPassword,
                        onValueChange = { loginViewModel.forgotNewPassword = it; loginViewModel.forgotNewPasswordError = null },
                        label = { Text("New Password") },
                        singleLine = true,
                        isError = loginViewModel.forgotNewPasswordError != null,
                        supportingText = {
                            loginViewModel.forgotNewPasswordError?.let { Text(it) }
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { loginViewModel.onConfirmResetPassword() }) {
                    Text("Reset Password")
                }
            },
            dismissButton = {
                TextButton(onClick = { loginViewModel.onDismissForgotPasswordDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun Loginscreenpreview() {
    LoginScreen(
        onLoginClick = { _ -> },
        onSignupClick = {},
        onAdminSignupClick = {},
        onGoogleLoginClick = {}
    )
}
