package com.autochecker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.theme.*

@Composable
fun AppearanceScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val theme by viewModel.theme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        AutoCheckerTopBar(
            title = stringResource(R.string.appearance_title),
            onBack = onBack,
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.appearance_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = AutoCheckerTheme.colors.textSecondary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ThemeOptionCard(
                    icon = Icons.Filled.DarkMode,
                    title = stringResource(R.string.theme_dark),
                    selected = theme == "dark",
                    onClick = { viewModel.setTheme("dark") },
                    modifier = Modifier.weight(1f),
                )
                ThemeOptionCard(
                    icon = Icons.Filled.LightMode,
                    title = stringResource(R.string.theme_light),
                    selected = theme == "light",
                    onClick = { viewModel.setTheme("light") },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) AccentRed.copy(alpha = 0.1f) else AutoCheckerTheme.colors.card)
            .clickable(onClick = onClick)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) AccentRed else AutoCheckerTheme.colors.textSecondary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = if (selected) AutoCheckerTheme.colors.textPrimary else AutoCheckerTheme.colors.textSecondary,
        )
    }
}
