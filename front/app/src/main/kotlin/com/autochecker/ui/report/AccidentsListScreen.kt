package com.autochecker.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.autochecker.ui.components.AccidentCard
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.components.LoadingOverlay
import com.autochecker.ui.theme.*

@Composable
fun AccidentsListScreen(
    onBack: () -> Unit,
    onAccidentClick: (String) -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val state by viewModel.reportState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AutoCheckerTheme.colors.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            AutoCheckerTopBar(
                title = stringResource(R.string.accidents_list_title),
                onBack = onBack,
            )

            state.error?.let { error ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
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
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.accidents_count, report.accidentCount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AutoCheckerTheme.colors.textSecondary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(report.accidents) { accident ->
                        AccidentCard(
                            accident = accident,
                            onClick = { onAccidentClick(accident.guid) },
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}
