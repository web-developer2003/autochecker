package com.autochecker.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.autochecker.ui.components.AutoCheckerCard
import com.autochecker.ui.components.ProfileMenuItem
import com.autochecker.ui.theme.*

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    onSavedVehicles: () -> Unit,
    onNotifications: () -> Unit,
    onAppearance: () -> Unit,
    onLanguage: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onTermsOfService: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.profileState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
            color = AutoCheckerTheme.colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // User info card
        state.user?.let { user ->
            AutoCheckerCard(onClick = onEditProfile) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AutoCheckerTheme.colors.cardVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.fullName.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = AccentRed,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = user.fullName,
                            style = MaterialTheme.typography.titleMedium,
                            color = AutoCheckerTheme.colors.textPrimary,
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = AutoCheckerTheme.colors.textSecondary,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu items
        AutoCheckerCard {
            ProfileMenuItem(
                icon = Icons.Filled.DirectionsCar,
                title = stringResource(R.string.saved_vehicles),
                onClick = onSavedVehicles,
            )
            HorizontalDivider(color = AutoCheckerTheme.colors.divider)
            ProfileMenuItem(
                icon = Icons.Filled.Notifications,
                title = stringResource(R.string.notifications_title),
                onClick = onNotifications,
            )
            HorizontalDivider(color = AutoCheckerTheme.colors.divider)
            ProfileMenuItem(
                icon = Icons.Filled.Palette,
                title = stringResource(R.string.appearance_title),
                onClick = onAppearance,
            )
            HorizontalDivider(color = AutoCheckerTheme.colors.divider)
            ProfileMenuItem(
                icon = Icons.Filled.Language,
                title = stringResource(R.string.language_title),
                onClick = onLanguage,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerCard {
            ProfileMenuItem(
                icon = Icons.Filled.PrivacyTip,
                title = stringResource(R.string.privacy_policy_title),
                onClick = onPrivacyPolicy,
            )
            HorizontalDivider(color = AutoCheckerTheme.colors.divider)
            ProfileMenuItem(
                icon = Icons.Filled.Description,
                title = stringResource(R.string.terms_title),
                onClick = onTermsOfService,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerCard {
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = stringResource(R.string.logout),
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                iconTint = StatusRed,
                titleColor = StatusRed,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
