package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Outfit
import com.example.dressmeapp.ui.components.AddFab
import com.example.dressmeapp.ui.components.ItemCard
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.OutfitViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenuesScreen(
    padding: PaddingValues,
    clothesViewModel: ClothesViewModel,
    outfitsVM: OutfitViewModel,
    onAddOutfitClick: () -> Unit
) {
    val allOutfit by outfitsVM.allOutfits.observeAsState(emptyList())
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())

    // Déterminer la saison actuelle
    val currentSeason = getCurrentSeason()
    var selectedSeasons by remember { mutableStateOf(setOf(currentSeason)) }
    var showSeasonMenu by remember { mutableStateOf(false) }
    val seasons = SaisonEnum.entries.map { it.label }

    // Filtrer les tenues selon les saisons sélectionnées
    val filteredOutfits = allOutfit.filter { outfit ->
        outfit.seasons.any { selectedSeasons.contains(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Titre principal
            item {
                Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ) {
                    PageTitle("Toutes les tenues",
                        modifier = Modifier.weight(1f),
                        fillMaxWidth = false)

                    Box {
                        IconButton(onClick = { showSeasonMenu = !showSeasonMenu }) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                                contentDescription = "Filtrer par saison",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Menu déroulant
                        DropdownMenu(
                            expanded = showSeasonMenu,
                            onDismissRequest = { showSeasonMenu = false },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(Dimensions.spacing12),
                                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
                            ) {
                                seasons.forEach { season ->
                                    FilterChip(
                                        selected = selectedSeasons.contains(season),
                                        onClick = {
                                            selectedSeasons = if (selectedSeasons.contains(season)) {
                                                selectedSeasons - season
                                            } else {
                                                selectedSeasons + season
                                            }
                                        },
                                        label = { Text(season) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(filteredOutfits) { outfit ->
                OutfitCardCompact(
                    outfit = outfit,
                    allClothes = allClothes,
                    onDelete = { outfitsVM.deleteOutfit(outfit) }
                )
            }

            // Espace en bas pour FAB
            item { Spacer(modifier = Modifier.height(Dimensions.spacing32)) }
        }

        AddFab(
            onClick = onAddOutfitClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimensions.spacing16),
            contentDescription = "Ajouter une tenue"
        )
    }
}

@Composable
fun OutfitCardCompact(
    outfit: Outfit,
    allClothes: List<Clothes>,
    onDelete: () -> Unit
) {
    // Convertit "1,2,8" → [1,2,8]
    val clothesIds = outfit.clothesIds
        .split(",")
        .mapNotNull { it.toIntOrNull() }

    // Récupération des objets Clothes correspondants
    val outfitClothes = allClothes.filter { clothesIds.contains(it.id) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerLarge),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimensions.elevationSmall
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacing12),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
        ) {
            // Titre + poubelle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = outfit.nom.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                        contentDescription = "Supprimer la tenue",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Saison
            val label = if (outfit.seasons.size > 1) "Saisons" else "Saison"
            Text(
                text = "$label : ${outfit.seasons.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Images des vêtements
            if (outfitClothes.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
                ) {
                    outfitClothes.forEach { cloth ->
                        AsyncImage(
                            model = cloth.imageUri,
                            contentDescription = cloth.nom,
                            modifier = Modifier.size(Dimensions.spacing100)
                        )
                    }
                }
            } else {
                Text(
                    "Aucun vêtement enregistré",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getCurrentSeason(): String {
    val month = LocalDate.now().monthValue
    return when (month) {
        3, 4, 5 -> SaisonEnum.PRINTEMPS.label
        6, 7, 8 -> SaisonEnum.ETE.label
        9, 10, 11 -> SaisonEnum.AUTOMNE.label
        else -> SaisonEnum.HIVER.label
    }
}