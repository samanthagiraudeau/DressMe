package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.ui.components.DressMeChipGroup
import com.example.dressmeapp.ui.components.DressMeTextField
import com.example.dressmeapp.ui.components.ExpandableSection
import com.example.dressmeapp.ui.components.PrimaryButton
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.OutfitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOutfitScreen(
    padding: PaddingValues,
    clothesViewModel: ClothesViewModel,
    outfitsVM: OutfitViewModel,
    onBack: () -> Unit
) {
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())

    var outfitName by remember { mutableStateOf("") }
    val seasons = SaisonEnum.entries.map { it.label }
    val selectedSeasons = remember { mutableStateListOf<String>() }

    // Liste des IDs sélectionnés
    val selectedIds = remember { mutableStateListOf<Int>() }

    // Filtrage ultra simple
    val bas = allClothes.filter { it.category == "bas" }
    val global = allClothes.filter { it.category == "global" }
    val pulls = allClothes.filter { it.subCategory == "pull" || it.subCategory == "gilet" }
    val tshirts = allClothes.filter { it.subCategory == "tee-shirt" }
    val shoes = allClothes.filter { it.category == "chaussures" }
    val manteaux = allClothes.filter { it.category == "manteau" }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Ajouter une tenue") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Nom de la tenue
            item {
                DressMeTextField(
                    value = outfitName,
                    onValueChange = { outfitName = it },
                    label = "Label"
                )
            }

            // Saison
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Saison",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = Dimensions.spacing8)
                    )
                    DressMeChipGroup(
                        items = seasons,
                        selectedItems = selectedSeasons.toList(),
                        onItemChange = { item, isSelected ->
                            if (isSelected) selectedSeasons.add(item)
                            else selectedSeasons.remove(item)
                        }
                    )
                }
            }

            // Listes de sélection de vêtements
            if (bas.isNotEmpty()) {
                item {
                    ClothingSelectorSection("Bas", bas, selectedIds)
                }
            }

            if (global.isNotEmpty()) {
                item {
                    ClothingSelectorSection("Global", global, selectedIds)
                }
            }

            if (pulls.isNotEmpty()) {
                item {
                    ClothingSelectorSection("Pulls / Gilets", pulls, selectedIds)
                }
            }

            if (tshirts.isNotEmpty()) {
                item {
                    ClothingSelectorSection("Tee-shirts", tshirts, selectedIds)
                }
            }

            if (shoes.isNotEmpty()) {
                item {
                    ClothingSelectorSection("Chaussures", shoes, selectedIds)
                }
            }

            if (manteaux.isNotEmpty()) {
                item {
                    ClothingSelectorSection("Manteaux", manteaux, selectedIds)
                }
            }

            // Bouton Enregistrer
            item {
                PrimaryButton(
                    text = "Enregistrer la tenue",
                    onClick = {
                        outfitsVM.saveOutfit(
                            name = outfitName,
                            seasons = selectedSeasons.toList(),
                            clothesIds = selectedIds.toList()
                        )
                        onBack()
                    },
                    enabled = outfitName.isNotBlank() && selectedSeasons.isNotEmpty() && selectedIds.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Espace en bas
            item { Spacer(modifier = Modifier.height(Dimensions.spacing16)) }
        }
    }
}

@Composable
fun ClothingSelectorSection(
    title: String,
    items: List<Clothes>,
    selectedIds: MutableList<Int>
) {
    ExpandableSection(
        title = title,
        initiallyExpanded = true
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
        ) {
            items.forEach { cloth ->
                val isSelected = selectedIds.contains(cloth.id)

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerMedium),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = if (isSelected) Dimensions.elevationMedium else Dimensions.elevationSmall
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimensions.spacing12),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
                        ) {
                            AsyncImage(
                                model = cloth.imageUri,
                                contentDescription = cloth.nom,
                                modifier = Modifier.size(Dimensions.spacing60)
                            )

                            Text(
                                text = when {
                                    !cloth.nom.isNullOrBlank() -> "${cloth.nom} - ${cloth.color}"
                                    !cloth.subCategory.isNullOrBlank() -> "${cloth.category} - ${cloth.subCategory} - ${cloth.color}"
                                    else -> "${cloth.category} - ${cloth.color}"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = {
                                if (isSelected) selectedIds.remove(cloth.id)
                                else selectedIds.add(cloth.id)
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected)
                                    Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                                contentDescription = if (isSelected) "Sélectionné" else "Non sélectionné",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
