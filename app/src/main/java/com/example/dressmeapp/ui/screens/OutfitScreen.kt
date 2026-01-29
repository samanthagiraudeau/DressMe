package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.ui.components.DressMeChipGroup
import com.example.dressmeapp.ui.components.DressMeTextField
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.ui.components.PrimaryButton
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.OutfitViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel
import com.google.accompanist.flowlayout.FlowRow
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OutfitScreen(
    padding: PaddingValues,
    viewModel: ClothesViewModel,
    rulesViewModel: RulesViewModel,
    outfitViewModel: OutfitViewModel
) {
    val outfit by viewModel.outfit.collectAsState()
    val allRules by rulesViewModel.allRules.observeAsState(emptyList())

    var generatGlobalOutfit by rememberSaveable { mutableStateOf(false) }
    var generatGiletWithTeeShirt by rememberSaveable { mutableStateOf(true) }
    var selectedSeason by rememberSaveable { mutableStateOf(currentSeasonLabel()) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    var showSaveSheet by rememberSaveable { mutableStateOf(false) }

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
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacing8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Titre + settings button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PageTitle(
                        "Tenue aléatoire",
                        modifier = Modifier.weight(1f),
                        fillMaxWidth = false
                    )

                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Paramètres",
                            modifier = Modifier.size(Dimensions.iconSizeMedium),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Outfit preview
            item {
                FlowRow(
                    mainAxisSpacing = Dimensions.spacing16,
                    crossAxisSpacing = Dimensions.spacing16
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

        // Boutons d'action
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
            modifier = Modifier
                .padding(horizontal = Dimensions.spacing16)
                .padding(bottom = Dimensions.spacing16)
        ) {
            PrimaryButton(
                text = "Générer",
                onClick = {
                    viewModel.generateOutfit(
                        generatGlobalOutfit,
                        generatGiletWithTeeShirt,
                        allRules,
                        selectedSeason
                    )
                },
                modifier = Modifier.weight(1f)
            )

            FloatingActionButton(
                onClick = { showSaveSheet = true },
                modifier = Modifier.size(Dimensions.minTouchTarget),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.SaveAs, contentDescription = "Enregistrer")
            }
        }
    }

    // Modales
    if (showSettings) {
        ModalBottomSheet(onDismissRequest = { showSettings = false }, containerColor = MaterialTheme.colorScheme.surface) {
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

    if (showSaveSheet) {
        SaveOutfitSheet(
            onDismiss = { showSaveSheet = false },
            onSave = { label, seasons ->
                val clothesIds = listOfNotNull(
                    outfit?.global?.id,
                    outfit?.bas?.id,
                    outfit?.haut?.id,
                    outfit?.chaussures?.id,
                    outfit?.manteau?.id,
                    outfit?.teeShirt?.id
                )

                outfitViewModel.saveOutfit(
                    name = label,
                    seasons = seasons,
                    clothesIds = clothesIds
                )

                showSaveSheet = false
            }
        )
    }
}

@Composable
private fun OutfitSlot(title: String, uri: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(Dimensions.spacing120),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerMedium),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = Dimensions.elevationSmall
        ) {
            if (uri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.spacing4))
        Text(
            title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
            .padding(Dimensions.spacing24),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacing20)
    ) {
        Text("Paramètres", style = MaterialTheme.typography.headlineSmall)

        // Saison
        var expandedSeason by remember { mutableStateOf(false) }
        Column {
            Text(
                "Saison",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = Dimensions.spacing8)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerMedium)
                    )
                    .clickable { expandedSeason = true }
                    .padding(Dimensions.spacing12)
            ) {
                Text(selectedSeason, style = MaterialTheme.typography.bodySmall)
            }

            DropdownMenu(
                expanded = expandedSeason,
                onDismissRequest = { expandedSeason = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                SaisonEnum.entries.forEach { season ->
                    DropdownMenuItem(
                        text = { Text(season.label) },
                        onClick = {
                            onSeasonChange(season.label)
                            expandedSeason = false
                        }
                    )
                }
            }
        }

        // Robe ou combi
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Robe ou combi", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = generatGlobalOutfit,
                onCheckedChange = onGlobalOutfitChange
            )
        }

        // T-shirt sous gilet
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("T-shirt sous gilet", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = generatGiletWithTeeShirt,
                onCheckedChange = onGiletWithTeeShirtChange
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.spacing12))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveOutfitSheet(
    onDismiss: () -> Unit,
    onSave: (String, List<String>) -> Unit
) {
    var label by remember { mutableStateOf("") }
    val allSeasons = SaisonEnum.entries.map { it.label }
    val selectedSeasons = remember { mutableStateListOf<String>() }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing24),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16),
        ) {
            Text("Enregistrer la tenue", style = MaterialTheme.typography.headlineSmall)

            DressMeTextField(
                value = label,
                onValueChange = { label = it },
                label = "Nom de la tenue",
            )

            Text("Saisons", style = MaterialTheme.typography.titleSmall)
            DressMeChipGroup(
                items = allSeasons,
                selectedItems = selectedSeasons.toList(),
                onItemChange = { item, isSelected ->
                    if (isSelected) selectedSeasons.add(item)
                    else selectedSeasons.remove(item)
                }
            )

            PrimaryButton(
                text = "Enregistrer",
                onClick = { onSave(label, selectedSeasons.toList()) },
                enabled = label.isNotBlank() && selectedSeasons.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimensions.spacing16))
        }
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
