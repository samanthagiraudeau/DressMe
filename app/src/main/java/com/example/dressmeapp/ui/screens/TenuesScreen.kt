package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Outfit
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.OutfitViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

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




    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titre principal
            item { PageTitle("Toutes les tenues") }

            items(allOutfit) { outfit ->
                OutfitCard(
                    outfit,
                    allClothes = allClothes
                ) { toDelete ->
                    outfitsVM.deleteOutfit(toDelete)
                }
            }
        }

        FloatingActionButton(
            onClick = onAddOutfitClick,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Ajouter une tenue")
        }
    }
}

@Composable
fun OutfitCard(
    outfit: Outfit,
    allClothes: List<Clothes>,
    onDelete: (Outfit) -> Unit
) {
    // Convertit "1,2,8" → [1,2,8]
    val clothesIds = outfit.clothesIds
        .split(",")
        .mapNotNull { it.toIntOrNull() }

    // Récupération des objets Clothes correspondants
    val outfitClothes = allClothes.filter { clothesIds.contains(it.id) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            /* ---------- Titre + poubelle ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = outfit.nom.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = { onDelete(outfit) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Supprimer la tenue",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            /* ---------- Saison ---------- */
            val label = if (outfit.seasons.size > 1) "Saisons" else "Saison"

            Text(
                text = "$label : ${outfit.seasons.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )


            /* ---------- Images des vêtements ---------- */
            if (outfitClothes.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    outfitClothes.forEach { cloth ->
                        AsyncImage(
                            model = cloth.imageUri,
                            contentDescription = cloth.nom,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(1.dp)
                        )
                    }
                }
            } else {
                Text("Aucun vêtement enregistré", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}