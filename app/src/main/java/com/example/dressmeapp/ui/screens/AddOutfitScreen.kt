package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.OutfitViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

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
    var selectedSeasons = remember { mutableStateListOf<String>() }


    // Liste des IDs sélectionnés
    val selectedIds = remember { mutableStateListOf<Int>() }

    /* -------- Filtrage ultra simple -------- */
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* -------- Nom de la tenue -------- */
            item {
                OutlinedTextField(
                    value = outfitName,
                    onValueChange = { outfitName = it },
                    label = { Text("Label") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            /* -------- Saison -------- */
            item {
                Text("Saison", style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    seasons.forEach { s ->
                        FilterChip(
                            selected = selectedSeasons.contains(s),
                            onClick = {
                                if (selectedSeasons.contains(s)) selectedSeasons.remove(s)
                                else selectedSeasons.add(s)
                            },
                            label = { Text(s) }
                        )
                    }
                }
            }

            /* -------- Listes -------- */
            item { ClothingSelector("Bas", bas, selectedIds) }
            item { ClothingSelector("Global", global, selectedIds) }
            item { ClothingSelector("Pulls / Gilets", pulls, selectedIds) }
            item { ClothingSelector("Tee-shirts", tshirts, selectedIds) }
            item { ClothingSelector("Chaussures", shoes, selectedIds) }
            item { ClothingSelector("Manteaux", manteaux, selectedIds)}
            /* -------- Bouton Enregistrer -------- */
            item {
                Button(
                    onClick = {
                        outfitsVM.saveOutfit(
                            name = outfitName,
                            seasons = selectedSeasons.toList(),
                            clothesIds = selectedIds.toList()
                        )
                        onBack()
                    },
                    enabled = outfitName.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enregistrer la tenue")
                }
            }
        }
    }
}

@Composable
fun ClothingSelector(
    title: String,
    items: List<Clothes>,
    selectedIds: MutableList<Int>
) {
    if (items.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        items.forEach { cloth ->
            val isSelected = selectedIds.contains(cloth.id)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = cloth.imageUri,
                        contentDescription = cloth.nom,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        when {
                            !cloth.nom.isNullOrBlank() -> "${cloth.nom} - ${cloth.color}"
                            !cloth.subCategory.isNullOrBlank() -> "${cloth.category} - ${cloth.subCategory} - ${cloth.color}"
                            else -> "${cloth.category} - ${cloth.color}"
                        }
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
                        contentDescription = null
                    )
                }
            }
        }
    }
}
