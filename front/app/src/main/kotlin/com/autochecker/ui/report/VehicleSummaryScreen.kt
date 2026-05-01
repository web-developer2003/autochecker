package com.autochecker.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.*
import com.autochecker.ui.theme.*

@Composable
fun VehicleSummaryScreen(
    onBack: () -> Unit,
    onViewFullReport: (String) -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val state by viewModel.reportState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AutoCheckerTheme.colors.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AutoCheckerTopBar(
                title = stringResource(R.string.vehicle_summary_title),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { viewModel.saveVehicle() }) {
                        Icon(
                            imageVector = if (state.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (state.isSaved) AccentRed else AutoCheckerTheme.colors.textPrimary,
                        )
                    }
                }
            )

            state.error?.let { error ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        tint = AutoCheckerTheme.colors.textSecondary,
                        modifier = Modifier.size(64.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AutoCheckerTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            state.report?.let { report ->
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    // Vehicle info card
                    AutoCheckerCard {
                        Text(
                            text = "${report.vehicle.make} ${report.vehicle.model}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AutoCheckerTheme.colors.textPrimary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${report.vehicle.year} • ${report.vehicle.color} • ${report.vehicle.bodyType}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AutoCheckerTheme.colors.textSecondary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            StatusBadge(status = report.overallStatus)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick stats
                    AutoCheckerCard {
                        SectionHeader(title = stringResource(R.string.quick_stats))
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(stringResource(R.string.vin_label), report.vehicle.vin)
                        InfoRow(stringResource(R.string.plate_label), report.vehicle.plateNumber)
                        InfoRow(stringResource(R.string.engine_label), "${report.vehicle.engineType} ${report.vehicle.engineVolume}")
                        InfoRow(stringResource(R.string.transmission_label), report.vehicle.transmission)
                        InfoRow(stringResource(R.string.owners_label), report.ownerCount.toString())
                        InfoRow(stringResource(R.string.accidents_label), report.accidentCount.toString())
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Legal status
                    AutoCheckerCard {
                        SectionHeader(title = stringResource(R.string.legal_status))
                        Spacer(modifier = Modifier.height(8.dp))
                        val legal = report.legalStatus
                        CheckResultRow(
                            label = stringResource(R.string.check_stolen),
                            status = if (legal?.isStolen == true) CheckStatus.FAIL else CheckStatus.PASS,
                        )
                        CheckResultRow(
                            label = stringResource(R.string.check_pledge),
                            status = if (legal?.hasPledge == true) CheckStatus.WARNING else CheckStatus.PASS,
                        )
                        CheckResultRow(
                            label = stringResource(R.string.check_restrictions),
                            status = if (legal?.hasRestrictions == true) CheckStatus.WARNING else CheckStatus.PASS,
                        )
                        CheckResultRow(
                            label = stringResource(R.string.check_arrest),
                            status = if (legal?.hasArrest == true) CheckStatus.FAIL else CheckStatus.PASS,
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AutoCheckerButton(
                        text = stringResource(R.string.view_full_report),
                        onClick = { onViewFullReport(report.vehicle.vin) },
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AutoCheckerTheme.colors.textSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = AutoCheckerTheme.colors.textPrimary,
        )
    }
}
