package com.autochecker.ui.accident

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.autochecker.R
import com.autochecker.ui.components.AutoCheckerButton
import com.autochecker.ui.components.AutoCheckerTextField
import com.autochecker.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportAccidentScreen(
    onBack: () -> Unit,
    viewModel: ReportAccidentViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Camera URI
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraUri != null) {
            viewModel.updatePhotoUri(cameraUri)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updatePhotoUri(it) }
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            onBack()
        }
    }

    Scaffold(
        containerColor = AutoCheckerTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.report_accident),
                        color = AutoCheckerTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = AutoCheckerTheme.colors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AutoCheckerTheme.colors.background,
                ),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            // Plate number
            Text(
                text = stringResource(R.string.plate_label),
                style = MaterialTheme.typography.titleSmall,
                color = AutoCheckerTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AutoCheckerTextField(
                value = state.plateNumber,
                onValueChange = viewModel::updatePlateNumber,
                placeholder = stringResource(R.string.plate_placeholder),
                leadingIcon = {
                    Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Photo section
            Text(
                text = stringResource(R.string.accident_photo),
                style = MaterialTheme.typography.titleSmall,
                color = AutoCheckerTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (state.photoUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    AsyncImage(
                        model = state.photoUri,
                        contentDescription = "Accident photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    IconButton(
                        onClick = { viewModel.updatePhotoUri(null) },
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Remove",
                            tint = StatusRed,
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Take photo
                    PhotoOptionCard(
                        icon = Icons.Filled.CameraAlt,
                        label = stringResource(R.string.take_photo),
                        onClick = {
                            val photoFile = File.createTempFile("accident_", ".jpg", context.cacheDir)
                            cameraUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                photoFile,
                            )
                            cameraLauncher.launch(cameraUri!!)
                        },
                        modifier = Modifier.weight(1f),
                    )
                    // Pick from gallery
                    PhotoOptionCard(
                        icon = Icons.Filled.PhotoLibrary,
                        label = stringResource(R.string.choose_gallery),
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Severity selector
            Text(
                text = stringResource(R.string.severity_label),
                style = MaterialTheme.typography.titleSmall,
                color = AutoCheckerTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SeverityChip(
                    label = stringResource(R.string.severity_minor),
                    selected = state.severity == "minor",
                    color = StatusGreen,
                    onClick = { viewModel.updateSeverity("minor") },
                    modifier = Modifier.weight(1f),
                )
                SeverityChip(
                    label = stringResource(R.string.severity_moderate),
                    selected = state.severity == "moderate",
                    color = StatusYellow,
                    onClick = { viewModel.updateSeverity("moderate") },
                    modifier = Modifier.weight(1f),
                )
                SeverityChip(
                    label = stringResource(R.string.severity_major),
                    selected = state.severity == "major",
                    color = StatusRed,
                    onClick = { viewModel.updateSeverity("major") },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Location
            AutoCheckerTextField(
                value = state.location,
                onValueChange = viewModel::updateLocation,
                label = stringResource(R.string.location_label),
                placeholder = stringResource(R.string.location_placeholder),
                leadingIcon = {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            AutoCheckerTextField(
                value = state.description,
                onValueChange = viewModel::updateDescription,
                label = stringResource(R.string.description),
                placeholder = stringResource(R.string.accident_description_placeholder),
                singleLine = false,
                leadingIcon = {
                    Icon(Icons.Filled.Notes, contentDescription = null, tint = AutoCheckerTheme.colors.textSecondary)
                },
            )

            // Error
            state.error?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = StatusRed,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AutoCheckerButton(
                text = stringResource(R.string.submit_report),
                onClick = viewModel::submit,
                isLoading = state.isSubmitting,
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PhotoOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, AutoCheckerTheme.colors.inputBorder, RoundedCornerShape(12.dp))
            .background(AutoCheckerTheme.colors.card)
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentRed,
            modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = AutoCheckerTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun SeverityChip(
    label: String,
    selected: Boolean,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (selected) color.copy(alpha = 0.15f) else AutoCheckerTheme.colors.card
    val borderColor = if (selected) color else AutoCheckerTheme.colors.inputBorder
    val textColor = if (selected) color else AutoCheckerTheme.colors.textSecondary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
        )
    }
}
