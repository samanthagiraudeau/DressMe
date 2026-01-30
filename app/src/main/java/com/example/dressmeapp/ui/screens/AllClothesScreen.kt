package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.ui.components.AddFab
import com.example.dressmeapp.ui.components.ClothesCard
import com.example.dressmeapp.ui.components.ExpandableSection
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel
import java.io.File
import kotlin.toString

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
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Titre
            item { PageTitle("Mon dressing") }

            // Parcours des groupes
            grouped.forEach { (groupName, itemsInGroup) ->
                val expanded = expandedGroups[groupName] ?: true

                item {
                    ExpandableSection(
                        title = groupName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        initiallyExpanded = expanded,
                        modifier = Modifier.padding(horizontal = Dimensions.spacing0)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
                        ) {
                            itemsInGroup.forEach { item ->
                                ClothesCard(
                                    imageUrl = item.imageUri?.let { File(it).absolutePath },
                                    title = item.nom ?: item.subCategory ?: item.category,
                                    subtitle = buildString {
                                        append(item.color)
                                        if (!item.motif.equals("Aucun", ignoreCase = true)) {
                                            append(", ${item.motif}")
                                        }
                                        append(" - ${item.season.lowercase().replace("-", " ")}")
                                    },
                                    onDelete = { viewModel.deleteClothes(item) }
                                )
                            }
                        }
                    }
                }
            }

            // Espace en bas pour le FAB
            item { Spacer(Modifier.height(Dimensions.spacing32)) }
        }

        AddFab(
            onClick = onAddClotheClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimensions.spacing16),
            contentDescription = "Ajouter un vêtement"
        )
    }
}
