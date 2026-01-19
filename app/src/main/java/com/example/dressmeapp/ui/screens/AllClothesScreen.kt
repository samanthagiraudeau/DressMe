
package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.viewmodel.ClothesViewModel
import java.io.File
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import com.example.dressmeapp.ui.components.PageTitle


@Composable
fun AllClothesScreen(padding: PaddingValues, viewModel: ClothesViewModel) {
    val clothes by viewModel.allClothes.observeAsState(emptyList())

    // Grouper par sous-catégorie si présente, sinon par catégorie
    val grouped = clothes.groupBy { item ->
        if (!item.subCategory.isNullOrBlank()) item.subCategory!! else item.category
    }
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }


    LaunchedEffect(grouped.keys) {
        grouped.keys.forEach { key ->
            if (expandedGroups[key] == null) expandedGroups[key] = true
        }
    }


    LazyColumn( modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        item {
            PageTitle("Mon dressing")
        }
        grouped.forEach { (groupName, items) ->
            // ➜ État courant en lisant la map, avec défaut à TRUE
            val expanded = expandedGroups[groupName] ?: true

            // ✅ Header du groupe
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedGroups[groupName] = !expanded
                        }
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(groupName, style = MaterialTheme.typography.titleMedium)

                    Icon(
                        imageVector = if (expandedGroups[groupName] == true)
                            Icons.Filled.ExpandLess // flèche vers le haut si ouvert
                        else
                            Icons.Filled.ExpandMore, // flèche vers le bas si fermé
                        contentDescription = if (expandedGroups[groupName] == true) "Replier" else "Déplier"
                    )

                }
            }

            // ✅ Affichage des vêtements si groupe déplié
            if (expanded) {
                items(items) { item ->
                    ClothesCard(item) { toDelete ->
                        viewModel.deleteClothes(toDelete)
                    }
                }
            }
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}



@Composable
fun ClothesCard(item: Clothes, onDelete: (Clothes) -> Unit) {
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Image(
                    painter = rememberAsyncImagePainter(File(item.imageUri)),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    // ✅ Première ligne : nom > subCategory > category
                    val label = when {
                        !item.nom.isNullOrBlank() -> item.nom
                        !item.subCategory.isNullOrBlank() -> item.subCategory
                        else -> item.category
                    }
                    Text(label.lowercase(), style = MaterialTheme.typography.titleMedium)

                    // ✅ Deuxième ligne : couleur + motif si != "aucun"
                    val details = buildString {
                        append(item.color)
                        if (!item.motif.equals("Aucun", ignoreCase = true)) {
                            append(", ${item.motif}")
                        }
                    }
                    Text(details.lowercase(), style = MaterialTheme.typography.bodyMedium)

                    var labelSeason = item.season.lowercase().replace("-", " ")
                    Text(labelSeason, style = MaterialTheme.typography.bodyMedium)
                }
            }

            IconButton(onClick = { onDelete(item) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

        }
    }
}
