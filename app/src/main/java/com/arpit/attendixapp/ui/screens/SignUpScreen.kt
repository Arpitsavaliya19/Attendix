package com.arpit.attendixapp.ui.screens

import com.arpit.attendixapp.model.*
import com.arpit.attendixapp.ui.components.*
import com.arpit.attendixapp.ui.navigation.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.arpit.attendixapp.viewmodels.SignUpViewModel

class SignUpViewModelFactory(private val application: AttendixApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(application.userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    initialRole: String = "Student",
    onCreateAccountClick: (role: String) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as AttendixApplication
    val signUpViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(application)
    )

    // Set initial role only once
    androidx.compose.runtime.LaunchedEffect(initialRole) {
        signUpViewModel.onRoleChange(initialRole)
    }

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
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                roles.forEachIndexed { index, role ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = roles.size),
                        onClick = { signUpViewModel.onRoleChange(role) },
                        selected = signUpViewModel.selectedRole == role
                    ) {
                        Text(role)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = signUpViewModel.username,
                onValueChange = { signUpViewModel.onUsernameChange(it) },
                label = { Text("Username") },
                singleLine = true,
                isError = signUpViewModel.usernameError != null,
                supportingText = {
                    signUpViewModel.usernameError?.let { Text(it) }
                },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = signUpViewModel.email,
                onValueChange = { signUpViewModel.onEmailChange(it) },
                label = { Text("Email") },
                singleLine = true,
                isError = signUpViewModel.emailError != null,
                supportingText = {
                    signUpViewModel.emailError?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = signUpViewModel.password,
                onValueChange = { signUpViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                singleLine = true,
                isError = signUpViewModel.passwordError != null,
                supportingText = {
                    signUpViewModel.passwordError?.let { Text(it) }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    signUpViewModel.validateAndSignUp(onSuccess = {
                        onCreateAccountClick(signUpViewModel.selectedRole)
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = CircleShape
            ) {
                Text(text = "Sign Up as ${signUpViewModel.selectedRole}", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onCreateAccountClick = {})
}
