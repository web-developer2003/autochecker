package com.autochecker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.autochecker.ui.theme.*

@Composable
fun TimelineCard(
    date: String,
    title: String,
    subtitle: String? = null,
    isLast: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(AccentRed)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(AutoCheckerTheme.colors.divider)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = AutoCheckerTheme.colors.textTertiary,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = AutoCheckerTheme.colors.textPrimary,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AutoCheckerTheme.colors.textSecondary,
                )
            }
        }
    }
}
