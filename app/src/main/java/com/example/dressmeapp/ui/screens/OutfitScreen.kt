
package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel
import com.google.accompanist.flowlayout.FlowRow
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OutfitScreen(
    padding: PaddingValues,
    viewModel: ClothesViewModel,
    rulesViewModel: RulesViewModel
) {

    // ----- ViewModel states -----
    val outfit by viewModel.outfit.collectAsState()
    val allRules by rulesViewModel.allRules.observeAsState(emptyList())

    // ----- UI states -----
    var generatGlobalOutfit by rememberSaveable { mutableStateOf(false) }
    var generatGiletWithTeeShirt by rememberSaveable { mutableStateOf(true) }
    var selectedSeason by rememberSaveable { mutableStateOf(currentSeasonLabel()) }
    var showSettings by rememberSaveable { mutableStateOf(false) }

    // ----- Auto generate outfit -----
    LaunchedEffect(allRules, selectedSeason, generatGlobalOutfit, generatGiletWithTeeShirt) {
        if (allRules.isNotEmpty()) {
            viewModel.generateOutfit(
                generatGlobalOutfit,
                generatGiletWithTeeShirt,
                allRules,
                selectedSeason
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ----- Title + settings button -----
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PageTitle("Tenue aléatoire", modifier = Modifier.weight(1f), fillMaxWidth = false)

                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Paramètres",
                            modifier = Modifier.size(24.dp),

                        )
                    }

                }
            }

            // ----- Outfit preview -----
            item {
                FlowRow(
                    mainAxisSpacing = 16.dp,
                    crossAxisSpacing = 16.dp
                ) {
                    OutfitSlot(
                        title = CategoryEnum.MANTEAU.label,
                        uri = outfit?.manteau?.imageUri
                    )

                    if (generatGlobalOutfit) {
                        OutfitSlot(
                            title = CategoryEnum.GLOBAL.label,
                            uri = outfit?.global?.imageUri
                        )
                    } else {
                        OutfitSlot(title = CategoryEnum.HAUT.label, uri = outfit?.haut?.imageUri)
                        OutfitSlot(title = CategoryEnum.BAS.label, uri = outfit?.bas?.imageUri)

                        if (generatGiletWithTeeShirt && outfit?.teeShirt != null) {
                            OutfitSlot(
                                title = SubCategoryEnum.TEESHIRT.label,
                                uri = outfit?.teeShirt?.imageUri
                            )
                        }
                    }

                    OutfitSlot(
                        title = CategoryEnum.CHAUSSURES.label,
                        uri = outfit?.chaussures?.imageUri
                    )
                }
            }
        }

        // ----- Force regenerate -----
        Button(
            onClick = {
                viewModel.generateOutfit(
                    generatGlobalOutfit,
                    generatGiletWithTeeShirt,
                    allRules,
                    selectedSeason
                )
            }
        ) {
            Text("Générer une autre tenue")
        }

        Spacer(Modifier.height(2.dp))
    }

    // ===== MODAL BOTTOM SHEET =====

    if (showSettings) {
        ModalBottomSheet(
            onDismissRequest = { showSettings = false }
        ) {
            SettingsContent(
                selectedSeason = selectedSeason,
                onSeasonChange = { selectedSeason = it },

                generatGlobalOutfit = generatGlobalOutfit,
                onGlobalOutfitChange = { generatGlobalOutfit = it },

                generatGiletWithTeeShirt = generatGiletWithTeeShirt,
                onGiletWithTeeShirtChange = { generatGiletWithTeeShirt = it }
            )
        }
    }
}

@Composable
private fun OutfitSlot(title: String, uri: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(title)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(title, style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenuBoxSample(
    label: String,
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = value,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsContent(
    selectedSeason: String,
    onSeasonChange: (String) -> Unit,
    generatGlobalOutfit: Boolean,
    onGlobalOutfitChange: (Boolean) -> Unit,
    generatGiletWithTeeShirt: Boolean,
    onGiletWithTeeShirtChange: (Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("Paramètres", style = MaterialTheme.typography.headlineSmall)

        // Saison
        ExposedDropdownMenuBoxSample(
            label = "Saison",
            options = SaisonEnum.entries.map { it.label },
            value = selectedSeason,
            onValueChange = onSeasonChange
        )

        // Robe ou combi
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Robe ou combi")
            Switch(
                checked = generatGlobalOutfit,
                onCheckedChange = onGlobalOutfitChange
            )
        }

        // T-shirt sous gilet
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("T-shirt sous gilet")
            Switch(
                checked = generatGiletWithTeeShirt,
                onCheckedChange = onGiletWithTeeShirtChange
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}

fun currentSeasonLabel(): String {
    val month = LocalDate.now().monthValue
    return when (month) {
        12, 1, 2 -> SaisonEnum.HIVER.label
        3, 4, 5 -> SaisonEnum.PRINTEMPS.label
        6, 7, 8 -> SaisonEnum.ETE.label
        9, 10, 11 -> SaisonEnum.AUTOMNE.label
        else -> SaisonEnum.HIVER.label
    }
}
