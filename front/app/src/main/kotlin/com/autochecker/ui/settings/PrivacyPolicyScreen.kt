package com.autochecker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.theme.AutoCheckerTheme

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        AutoCheckerTopBar(
            title = stringResource(R.string.privacy_policy_title),
            onBack = onBack,
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.privacy_policy_updated),
                style = MaterialTheme.typography.bodySmall,
                color = AutoCheckerTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.privacy_policy_content),
                style = MaterialTheme.typography.bodyMedium,
                color = AutoCheckerTheme.colors.textPrimary,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
            )
        }
    }
}
