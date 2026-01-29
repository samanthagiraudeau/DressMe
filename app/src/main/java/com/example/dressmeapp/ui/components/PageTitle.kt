
package com.example.dressmeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun PageTitle(
    text: String,
    modifier: Modifier = Modifier,
    center: Boolean = true,
    showDivider: Boolean = true,
    fillMaxWidth: Boolean = true

) {

    val baseModifier = if (fillMaxWidth) {
        modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    } else {
        modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .heightIn(min = 24.dp, max = 64.dp)
    }


    Column(
        modifier = baseModifier,
        horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = if (center) TextAlign.Center else TextAlign.Start,
        )
        if (showDivider) {
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
        }
    }
}
