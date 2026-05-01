package com.autochecker.ui.vehicles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.components.LoadingOverlay
import com.autochecker.ui.components.VehicleCard
import com.autochecker.ui.theme.*

@Composable
fun SavedVehiclesScreen(
    onBack: () -> Unit,
    onVehicleClick: (String) -> Unit,
    viewModel: VehiclesViewModel = hiltViewModel(),
) {
    val state by viewModel.savedState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AutoCheckerTheme.colors.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            AutoCheckerTopBar(
                title = stringResource(R.string.saved_vehicles),
                onBack = onBack,
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (state.vehicles.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_saved_vehicles),
                                style = MaterialTheme.typography.bodyMedium,
                                color = AutoCheckerTheme.colors.textTertiary,
                            )
                        }
                    }
                }

                items(state.vehicles) { vehicle ->
                    VehicleCard(
                        vehicle = vehicle,
                        onClick = { onVehicleClick(vehicle.vin) },
                        trailing = {
                            IconButton(onClick = { viewModel.deleteVehicle(vehicle.guid) }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = StatusRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}
