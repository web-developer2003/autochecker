package com.autochecker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.LoadingOverlay
import com.autochecker.ui.components.VehicleCard
import com.autochecker.ui.theme.*

@Composable
fun HistoryScreen(
    onVehicleClick: (String) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AutoCheckerTheme.colors.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.history_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = AutoCheckerTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.history_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AutoCheckerTheme.colors.textSecondary,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.items.isEmpty() && !state.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.history_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AutoCheckerTheme.colors.textTertiary,
                        )
                    }
                }
            }

            items(state.items) { item ->
                VehicleCard(
                    vehicle = item.vehicle.toVehicle(),
                    onClick = { onVehicleClick(item.vin) },
                    trailing = {
                        Text(
                            text = item.searchedAt,
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoCheckerTheme.colors.textTertiary,
                        )
                    }
                )
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}
