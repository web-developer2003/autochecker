package com.autochecker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerTopBar
import com.autochecker.ui.components.LanguageRadioItem
import com.autochecker.ui.theme.AutoCheckerTheme

@Composable
fun LanguageScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val language by viewModel.language.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AutoCheckerTheme.colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        AutoCheckerTopBar(
            title = stringResource(R.string.language_title),
            onBack = onBack,
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.language_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = AutoCheckerTheme.colors.textSecondary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            LanguageRadioItem(
                title = "O'zbek tili",
                subtitle = "Uzbek",
                selected = language == "uz",
                onClick = { viewModel.setLanguage("uz") },
            )
            Spacer(modifier = Modifier.height(12.dp))
            LanguageRadioItem(
                title = "Русский язык",
                subtitle = "Russian",
                selected = language == "ru",
                onClick = { viewModel.setLanguage("ru") },
            )
            Spacer(modifier = Modifier.height(12.dp))
            LanguageRadioItem(
                title = "English",
                subtitle = "English",
                selected = language == "en",
                onClick = { viewModel.setLanguage("en") },
            )
        }
    }
}
