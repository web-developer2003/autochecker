package com.autochecker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerButton
import com.autochecker.ui.components.AutoCheckerTextField
import com.autochecker.ui.components.FeatureCard
import com.autochecker.ui.theme.*

@Composable
fun HomeScreen(
    onSearchResult: (String) -> Unit,
    onReportAccident: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.searchResult) {
        state.searchResult?.let {
            onSearchResult(it.vehicle.vin)
            viewModel.clearResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.home_greeting),
            style = MaterialTheme.typography.headlineMedium,
            color = AutoCheckerTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = AutoCheckerTheme.colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Search source selector
        Text(
            text = stringResource(R.string.search_source),
            style = MaterialTheme.typography.titleSmall,
            color = AutoCheckerTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SearchSourceChip(
                label = stringResource(R.string.source_local),
                icon = Icons.Filled.Storage,
                selected = state.searchSource == SearchSource.LOCAL,
                onClick = { viewModel.updateSearchSource(SearchSource.LOCAL) },
                modifier = Modifier.weight(1f),
            )
            SearchSourceChip(
                label = stringResource(R.string.source_government),
                icon = Icons.Filled.AccountBalance,
                selected = state.searchSource == SearchSource.GOVERNMENT,
                onClick = { viewModel.updateSearchSource(SearchSource.GOVERNMENT) },
                modifier = Modifier.weight(1f),
            )
            SearchSourceChip(
                label = stringResource(R.string.source_internet),
                icon = Icons.Filled.Language,
                selected = state.searchSource == SearchSource.INTERNET,
                onClick = { viewModel.updateSearchSource(SearchSource.INTERNET) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search type toggle (Plate / VIN)
        Text(
            text = stringResource(R.string.search_type_label),
            style = MaterialTheme.typography.titleSmall,
            color = AutoCheckerTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, AutoCheckerTheme.colors.inputBorder, RoundedCornerShape(10.dp)),
        ) {
            SearchTypeTab(
                label = stringResource(R.string.search_type_plate),
                selected = state.searchType == SearchType.PLATE,
                onClick = { viewModel.updateSearchType(SearchType.PLATE) },
                modifier = Modifier.weight(1f),
            )
            SearchTypeTab(
                label = stringResource(R.string.search_type_vin),
                selected = state.searchType == SearchType.VIN,
                onClick = { viewModel.updateSearchType(SearchType.VIN) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search input
        val isVin = state.searchType == SearchType.VIN
        AutoCheckerTextField(
            value = state.searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            placeholder = stringResource(if (isVin) R.string.vin_placeholder else R.string.plate_search_placeholder),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
            },
            isError = state.searchError != null,
            errorMessage = state.searchError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(onSearch = { viewModel.search() }),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoCheckerButton(
            text = stringResource(R.string.search_button),
            onClick = viewModel::search,
            isLoading = state.isSearching,
        )

        // Source message (for government/internet not available yet)
        state.sourceMessage?.let { msg ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = msg,
                style = MaterialTheme.typography.bodySmall,
                color = AutoCheckerTheme.colors.statusYellow,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Report accident button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AccentRed.copy(alpha = 0.1f))
                .clickable(onClick = onReportAccident)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AccentRed.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = null,
                    tint = AccentRed,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.report_accident),
                    style = MaterialTheme.typography.titleSmall,
                    color = AutoCheckerTheme.colors.textPrimary,
                )
                Text(
                    text = stringResource(R.string.report_accident_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = AutoCheckerTheme.colors.textSecondary,
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AutoCheckerTheme.colors.textSecondary,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.what_we_check),
            style = MaterialTheme.typography.titleLarge,
            color = AutoCheckerTheme.colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        FeatureCard(
            icon = Icons.Filled.CarCrash,
            title = stringResource(R.string.feature_accidents),
            subtitle = stringResource(R.string.feature_accidents_desc),
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureCard(
            icon = Icons.Filled.Gavel,
            title = stringResource(R.string.feature_legal),
            subtitle = stringResource(R.string.feature_legal_desc),
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureCard(
            icon = Icons.Filled.Speed,
            title = stringResource(R.string.feature_mileage),
            subtitle = stringResource(R.string.feature_mileage_desc),
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureCard(
            icon = Icons.Filled.Build,
            title = stringResource(R.string.feature_service),
            subtitle = stringResource(R.string.feature_service_desc),
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureCard(
            icon = Icons.Filled.Security,
            title = stringResource(R.string.feature_insurance),
            subtitle = stringResource(R.string.feature_insurance_desc),
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SearchTypeTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AutoCheckerTheme.colors
    Box(
        modifier = modifier
            .background(if (selected) AccentRed else colors.card)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) androidx.compose.ui.graphics.Color.White else colors.textSecondary,
        )
    }
}

@Composable
private fun SearchSourceChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AutoCheckerTheme.colors
    val backgroundColor = if (selected) AccentRed.copy(alpha = 0.15f) else colors.card
    val borderColor = if (selected) AccentRed else colors.inputBorder
    val textColor = if (selected) AccentRed else colors.textSecondary
    val iconColor = if (selected) AccentRed else colors.textTertiary

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}
