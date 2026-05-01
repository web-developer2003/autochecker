package com.autochecker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerCard
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.components.SectionHeader
import com.autochecker.ui.components.ToggleRow
import com.autochecker.ui.theme.AutoCheckerTheme

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val pushReportReady by viewModel.pushReportReady.collectAsState()
    val pushPromotions by viewModel.pushPromotions.collectAsState()
    val pushUpdates by viewModel.pushUpdates.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        AutoCheckerTopBar(
            title = stringResource(R.string.notifications_title),
            onBack = onBack,
        )

        Column(modifier = Modifier.padding(24.dp)) {
            AutoCheckerCard {
                ToggleRow(
                    title = stringResource(R.string.notifications_enable),
                    subtitle = stringResource(R.string.notifications_enable_desc),
                    checked = notificationsEnabled,
                    onCheckedChange = viewModel::setNotificationsEnabled,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AutoCheckerCard {
                SectionHeader(title = stringResource(R.string.push_notifications))
                ToggleRow(
                    title = stringResource(R.string.push_report_ready),
                    subtitle = stringResource(R.string.push_report_ready_desc),
                    checked = pushReportReady,
                    onCheckedChange = viewModel::setPushReportReady,
                )
                HorizontalDivider(color = AutoCheckerTheme.colors.divider)
                ToggleRow(
                    title = stringResource(R.string.push_promotions),
                    subtitle = stringResource(R.string.push_promotions_desc),
                    checked = pushPromotions,
                    onCheckedChange = viewModel::setPushPromotions,
                )
                HorizontalDivider(color = AutoCheckerTheme.colors.divider)
                ToggleRow(
                    title = stringResource(R.string.push_updates),
                    subtitle = stringResource(R.string.push_updates_desc),
                    checked = pushUpdates,
                    onCheckedChange = viewModel::setPushUpdates,
                )
            }
        }
    }
}
