package com.autochecker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerButton
import com.autochecker.ui.components.AutoCheckerTextField
import com.autochecker.ui.theme.*

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.signUpState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSignUpSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = stringResource(R.string.signup_title),
            style = MaterialTheme.typography.headlineMedium,
            color = AutoCheckerTheme.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.signup_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = AutoCheckerTheme.colors.textSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        AutoCheckerTextField(
            value = state.fullName,
            onValueChange = viewModel::updateSignUpFullName,
            label = stringResource(R.string.full_name),
            placeholder = stringResource(R.string.full_name_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerTextField(
            value = state.email,
            onValueChange = viewModel::updateSignUpEmail,
            label = stringResource(R.string.email),
            placeholder = stringResource(R.string.email_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerTextField(
            value = state.phone,
            onValueChange = viewModel::updateSignUpPhone,
            label = stringResource(R.string.phone),
            placeholder = stringResource(R.string.phone_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Phone, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerTextField(
            value = state.password,
            onValueChange = viewModel::updateSignUpPassword,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    tint = AutoCheckerTheme.colors.textSecondary,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::updateSignUpConfirmPassword,
            label = stringResource(R.string.confirm_password),
            placeholder = stringResource(R.string.confirm_password_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.error!!,
                style = MaterialTheme.typography.bodySmall,
                color = StatusRed,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AutoCheckerButton(
            text = stringResource(R.string.signup_button),
            onClick = viewModel::signUp,
            isLoading = state.isLoading,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.have_account))
                append(" ")
                withStyle(SpanStyle(color = AccentRed)) {
                    append(stringResource(R.string.login_link))
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable(onClick = onNavigateToLogin)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
