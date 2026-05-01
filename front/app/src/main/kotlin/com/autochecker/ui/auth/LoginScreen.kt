package com.autochecker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerButton
import com.autochecker.ui.components.AutoCheckerTextField
import com.autochecker.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.loginState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = AccentRed,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = AutoCheckerTheme.colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
            color = AutoCheckerTheme.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AutoCheckerTextField(
            value = state.email,
            onValueChange = viewModel::updateLoginEmail,
            label = stringResource(R.string.email),
            placeholder = stringResource(R.string.email_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerTextField(
            value = state.password,
            onValueChange = viewModel::updateLoginPassword,
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
            text = stringResource(R.string.login_button),
            onClick = viewModel::login,
            isLoading = state.isLoading,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.no_account))
                append(" ")
                withStyle(SpanStyle(color = AccentRed)) {
                    append(stringResource(R.string.sign_up_link))
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable(onClick = onNavigateToSignUp)
        )
    }
}
