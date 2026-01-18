
package com.example.dressmeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PageTitle(
    text: String,
    modifier: Modifier = Modifier,
    center: Boolean = true,
    showDivider: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = if (center) TextAlign.Center else TextAlign.Start
        )
        if (showDivider) {
            Spacer(Modifier.height(8.dp))
            Divider()
        }
    }
}
