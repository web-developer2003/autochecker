package com.autochecker.ui.vehicles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerButton
import com.autochecker.ui.components.AutoCheckerTextField
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.theme.*

@Composable
fun AddVehicleScreen(
    onBack: () -> Unit,
    viewModel: VehiclesViewModel = hiltViewModel(),
) {
    val state by viewModel.addState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        AutoCheckerTopBar(
            title = stringResource(R.string.add_vehicle_title),
            onBack = onBack,
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.add_vehicle_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            AutoCheckerTextField(
                value = state.name,
                onValueChange = viewModel::updateName,
                label = stringResource(R.string.vehicle_name),
                placeholder = stringResource(R.string.vehicle_name_placeholder),
                leadingIcon = {
                    Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = TextSecondary)
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            AutoCheckerTextField(
                value = state.vin,
                onValueChange = viewModel::updateVin,
                label = stringResource(R.string.vin_label),
                placeholder = stringResource(R.string.vin_placeholder),
                leadingIcon = {
                    Icon(Icons.Filled.Pin, contentDescription = null, tint = TextSecondary)
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            )

            Spacer(modifier = Modifier.height(16.dp))

            AutoCheckerTextField(
                value = state.plateNumber,
                onValueChange = viewModel::updatePlateNumber,
                label = stringResource(R.string.plate_label),
                placeholder = stringResource(R.string.plate_placeholder),
                leadingIcon = {
                    Icon(Icons.Filled.Badge, contentDescription = null, tint = TextSecondary)
                },
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = StatusRed,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AutoCheckerButton(
                text = stringResource(R.string.save_vehicle_button),
                onClick = viewModel::addVehicle,
                isLoading = state.isLoading,
            )
        }
    }
}
