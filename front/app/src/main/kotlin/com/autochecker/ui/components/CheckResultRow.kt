package com.autochecker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.autochecker.ui.theme.*

enum class CheckStatus { PASS, WARNING, FAIL }

@Composable
fun CheckResultRow(
    label: String,
    status: CheckStatus,
    detail: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = when (status) {
                CheckStatus.PASS -> Icons.Filled.CheckCircle
                CheckStatus.WARNING -> Icons.Filled.Warning
                CheckStatus.FAIL -> Icons.Filled.Cancel
            },
            contentDescription = null,
            tint = when (status) {
                CheckStatus.PASS -> StatusGreen
                CheckStatus.WARNING -> StatusYellow
                CheckStatus.FAIL -> StatusRed
            },
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = AutoCheckerTheme.colors.textPrimary,
            )
            if (detail != null) {
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = AutoCheckerTheme.colors.textSecondary,
                )
            }
        }
    }
}
