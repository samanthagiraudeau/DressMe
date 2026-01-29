package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel
import java.io.File

@Composable
fun AllClothesScreen(padding: PaddingValues, viewModel: ClothesViewModel, onAddClotheClick: () -> Unit) {
    val clothes by viewModel.allClothes.observeAsState(emptyList())

    // Grouper par sous-catégorie si présente, sinon par catégorie
    val grouped = clothes.groupBy { item ->
        if (!item.subCategory.isNullOrBlank()) item.subCategory!! else item.category
    }

    // État des groupes ouverts/fermés
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }

    // Initialisation : tous ouverts
    LaunchedEffect(grouped.keys) {
        grouped.keys.forEach { key ->
            if (expandedGroups[key] == null) expandedGroups[key] = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp), // moins d’espace vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titre
            item { PageTitle("Mon dressing") }

            // Parcours des groupes
            grouped.forEach { (groupName, itemsInGroup) ->
                val expanded = expandedGroups[groupName] ?: true

                // Header du groupe
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedGroups[groupName] = !expanded }
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(groupName, style = MaterialTheme.typography.titleMedium)
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (expanded) "Replier" else "Déplier"
                        )
                    }
                }

                // Items du groupe si ouvert
                if (expanded) {
                    items(itemsInGroup) { item ->
                        ClothesCard(item) { toDelete ->
                            viewModel.deleteClothes(toDelete)
                        }
                    }
                }
            }

            // Petit espace en bas
            item { Spacer(Modifier.height(8.dp)) }
        }

        FloatingActionButton(
            onClick = onAddClotheClick,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Ajouter un vêtement")
        }
    }

}

@Composable
fun ClothesCard(item: Clothes, onDelete: (Clothes) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Image + infos
            Row {
                Image(
                    painter = rememberAsyncImagePainter(File(item.imageUri)),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    // Nom / subCategory / category
                    val label = when {
                        !item.nom.isNullOrBlank() -> item.nom
                        !item.subCategory.isNullOrBlank() -> item.subCategory
                        else -> item.category
                    }
                    Text(label.lowercase(), style = MaterialTheme.typography.titleMedium)

                    // Détails couleur + motif
                    val details = buildString {
                        append(item.color)
                        if (!item.motif.equals("Aucun", ignoreCase = true)) append(", ${item.motif}")
                    }
                    Text(details.lowercase(), style = MaterialTheme.typography.bodyMedium)

                    // Saison
                    val labelSeason = item.season.lowercase().replace("-", " ")
                    Text(labelSeason, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Bouton supprimer
            IconButton(onClick = { onDelete(item) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
