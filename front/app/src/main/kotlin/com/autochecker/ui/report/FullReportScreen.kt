package com.autochecker.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun FullReportScreen(
    onBack: () -> Unit,
    onViewAccidents: (String) -> Unit,
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
                title = stringResource(R.string.full_report_title),
                onBack = onBack,
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
                    // Vehicle header
                    AutoCheckerCard {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${report.vehicle.make} ${report.vehicle.model}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = AutoCheckerTheme.colors.textPrimary,
                                )
                                Text(
                                    text = report.vehicle.vin,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AutoCheckerTheme.colors.textTertiary,
                                )
                            }
                            StatusBadge(status = report.overallStatus)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mileage history
                    AutoCheckerCard {
                        ExpandableSection(
                            title = stringResource(R.string.mileage_history),
                            initiallyExpanded = true,
                        ) {
                            report.mileageRecords.forEachIndexed { index, record ->
                                TimelineCard(
                                    date = record.date,
                                    title = "${record.mileage} km",
                                    subtitle = record.source,
                                    isLast = index == report.mileageRecords.lastIndex,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Accidents
                    if (report.accidents.isNotEmpty()) {
                        AutoCheckerCard {
                            SectionHeader(
                                title = "${stringResource(R.string.accidents_section)} (${report.accidentCount})"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.accidents_found, report.accidentCount),
                                style = MaterialTheme.typography.bodyMedium,
                                color = AutoCheckerTheme.colors.textSecondary,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AutoCheckerButton(
                                text = stringResource(R.string.view_accidents),
                                onClick = { onViewAccidents(report.vehicle.vin) },
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Service records
                    if (report.serviceRecords.isNotEmpty()) {
                        AutoCheckerCard {
                            ExpandableSection(title = stringResource(R.string.service_history)) {
                                report.serviceRecords.forEachIndexed { index, record ->
                                    TimelineCard(
                                        date = record.date,
                                        title = record.description,
                                        subtitle = record.location,
                                        isLast = index == report.serviceRecords.lastIndex,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}
