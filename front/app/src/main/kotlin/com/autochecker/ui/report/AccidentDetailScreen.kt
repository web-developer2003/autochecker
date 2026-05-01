package com.autochecker.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.autochecker.R
import com.autochecker.ui.components.*
import com.autochecker.ui.theme.*

@Composable
fun AccidentDetailScreen(
    onBack: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val state by viewModel.accidentDetailState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AutoCheckerTheme.colors.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AutoCheckerTopBar(
                title = stringResource(R.string.accident_detail_title),
                onBack = onBack,
            )

            state.accident?.let { accident ->
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    // Header
                    AutoCheckerCard {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = accident.date ?: "",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = AutoCheckerTheme.colors.textPrimary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = accident.location ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AutoCheckerTheme.colors.textSecondary,
                                )
                            }
                            SeverityBadge(severity = accident.severity)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Accident images
                    if (accident.imageUrls.isNotEmpty()) {
                        AccidentImageGallery(imageUrls = accident.imageUrls)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Description
                    AutoCheckerCard {
                        SectionHeader(title = stringResource(R.string.description))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = accident.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AutoCheckerTheme.colors.textPrimary,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Damaged areas
                    AutoCheckerCard {
                        SectionHeader(title = stringResource(R.string.damaged_areas))
                        Spacer(modifier = Modifier.height(8.dp))
                        accident.damageAreas?.forEach { area ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 6.dp)
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(AccentRed)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = area,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AutoCheckerTheme.colors.textPrimary,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Repair info
                    AutoCheckerCard {
                        SectionHeader(title = stringResource(R.string.repair_info))
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow(stringResource(R.string.repair_cost), accident.repairCost ?: "")
                        DetailRow(stringResource(R.string.repair_status), accident.repairStatus ?: "")
                        DetailRow(stringResource(R.string.police_report), accident.policeReportNumber ?: "")
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

@Composable
private fun AccidentImageGallery(imageUrls: List<String>) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    AutoCheckerCard {
        Column {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) { page ->
                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                )
            }

            if (imageUrls.size > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(imageUrls.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == pagerState.currentPage) AccentRed
                                    else AutoCheckerTheme.colors.textTertiary
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
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
