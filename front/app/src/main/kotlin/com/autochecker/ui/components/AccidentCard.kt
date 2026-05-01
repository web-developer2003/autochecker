package com.autochecker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.autochecker.data.model.Accident
import com.autochecker.ui.theme.*

@Composable
fun AccidentCard(
    accident: Accident,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AutoCheckerTheme.colors.card)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Thumbnail
        if (accident.imageUrls.isNotEmpty()) {
            AsyncImage(
                model = accident.imageUrls.first(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = accident.date ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = AutoCheckerTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                SeverityBadge(severity = accident.severity)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = accident.location ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = AutoCheckerTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = accident.description,
                style = MaterialTheme.typography.bodySmall,
                color = AutoCheckerTheme.colors.textTertiary,
                maxLines = 2,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = AutoCheckerTheme.colors.textTertiary,
            modifier = Modifier.size(16.dp)
        )
    }
}
