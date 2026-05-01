package com.autochecker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.autochecker.data.model.AccidentSeverity
import com.autochecker.data.model.ReportStatus
import com.autochecker.ui.theme.*

@Composable
fun StatusBadge(
    status: ReportStatus,
    modifier: Modifier = Modifier,
) {
    val (text, bgColor, textColor) = when (status) {
        ReportStatus.CLEAN -> Triple("Toza", StatusGreen.copy(alpha = 0.15f), StatusGreen)
        ReportStatus.WARNING -> Triple("Ogohlantirish", StatusYellow.copy(alpha = 0.15f), StatusYellow)
        ReportStatus.CRITICAL -> Triple("Xavfli", StatusRed.copy(alpha = 0.15f), StatusRed)
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun SeverityBadge(
    severity: AccidentSeverity?,
    modifier: Modifier = Modifier,
) {
    val (text, bgColor, textColor) = when (severity ?: AccidentSeverity.MINOR) {
        AccidentSeverity.MINOR -> Triple("Yengil", StatusGreen.copy(alpha = 0.15f), StatusGreen)
        AccidentSeverity.MODERATE -> Triple("O'rtacha", StatusYellow.copy(alpha = 0.15f), StatusYellow)
        AccidentSeverity.MAJOR -> Triple("Og'ir", StatusRed.copy(alpha = 0.15f), StatusRed)
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
