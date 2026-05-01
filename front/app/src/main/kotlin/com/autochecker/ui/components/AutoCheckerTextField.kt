package com.autochecker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.autochecker.ui.theme.*
import androidx.compose.ui.text.TextStyle

@Composable
fun AutoCheckerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
) {
    val colors = AutoCheckerTheme.colors
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label?.let { { Text(it) } },
        placeholder = placeholder?.let { { Text(it, color = colors.textHint) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = if (isError && errorMessage != null) {
            { Text(errorMessage, color = StatusRed) }
        } else null,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(color = colors.textPrimary),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedContainerColor = colors.inputBackground,
            unfocusedContainerColor = colors.inputBackground,
            focusedBorderColor = colors.inputBorderFocused,
            unfocusedBorderColor = colors.inputBorder,
            cursorColor = AccentRed,
            focusedLabelColor = AccentRed,
            unfocusedLabelColor = colors.textSecondary,
            errorBorderColor = StatusRed,
            errorContainerColor = colors.inputBackground,
            errorTextColor = colors.textPrimary,
            errorCursorColor = StatusRed,
            errorLabelColor = StatusRed,
        ),
    )
}
