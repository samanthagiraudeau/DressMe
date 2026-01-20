package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OutfitScreen(
    padding: PaddingValues,
    viewModel: ClothesViewModel,
    rulesViewModel: RulesViewModel
) {
    // état venant des ViewModels
    val outfit by viewModel.outfit.collectAsState()
    val allRules by rulesViewModel.allRules.observeAsState(emptyList())

    // état purement UI
    var generatGlobalOutfit by rememberSaveable { mutableStateOf(false) }
    var generatGiletWithTeeShirt by rememberSaveable { mutableStateOf(true) }
    var selectedSeason by rememberSaveable { mutableStateOf(currentSeasonLabel()) }

    // génération automatique quand les paramètres changent
    LaunchedEffect(
        allRules,
        selectedSeason,
        generatGlobalOutfit,
        generatGiletWithTeeShirt
    ) {
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
            item {
                PageTitle("Tenue aléatoire")
            }

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
                        OutfitSlot(
                            title = CategoryEnum.HAUT.label,
                            uri = outfit?.haut?.imageUri
                        )
                        OutfitSlot(
                            title = CategoryEnum.BAS.label,
                            uri = outfit?.bas?.imageUri
                        )

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

        ExposedDropdownMenuBoxSample(
            label = "Saison",
            options = SaisonEnum.entries.map { it.label },
            value = selectedSeason,
            onValueChange = { selectedSeason = it }
        )

        Spacer(Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Robe ou combi")
            Switch(
                checked = generatGlobalOutfit,
                onCheckedChange = { generatGlobalOutfit = it },
                modifier = Modifier.scale(0.7f)
            )

            Spacer(Modifier.width(8.dp))

            Text("T-shirt sous gilet")
            Switch(
                checked = generatGiletWithTeeShirt,
                onCheckedChange = { generatGiletWithTeeShirt = it },
                modifier = Modifier.scale(0.7f)
            )
        }

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
            modifier = Modifier
                .menuAnchor()
                .scale(0.7f),
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
