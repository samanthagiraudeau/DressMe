package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.dressmeapp.enums.ColorEnum
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.ui.components.DressMeDropdown
import com.example.dressmeapp.ui.components.PrimaryButton
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    padding: PaddingValues,
    viewModel: RulesViewModel,
    clothesViewModel: ClothesViewModel,
    onBack: () -> Unit
) {
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())
    val colorsList = ColorEnum.entries.map { it.label }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Ajouter une règle") },
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
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Section : Règles de couleurs
            item {
                AddColorRuleFormSection(
                    colors = colorsList,
                    onAdd = { c1, c2 ->
                        viewModel.addColorRule(c1, c2)
                        onBack()
                    }
                )
            }

            // Section : Règles de vêtements
            item {
                AddClothesRuleFormSection(
                    clothes = allClothes,
                    onAdd = { v1, v2 ->
                        viewModel.addClothesRule(v1.toString(), v2.toString())
                        onBack()
                    }
                )
            }
        }
    }
}

@Composable
private fun AddColorRuleFormSection(
    colors: List<String>,
    onAdd: (String, String) -> Unit
) {
    var color1 by remember { mutableStateOf(colors.firstOrNull().orEmpty()) }
    var color2 by remember { mutableStateOf(colors.getOrElse(1) { colors.firstOrNull().orEmpty() }) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerLarge),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimensions.elevationSmall
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacing16),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
        ) {
            Text(
                "Règles de couleurs",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            DressMeDropdown(
                value = color1,
                onValueChange = { color1 = it },
                label = "Couleur 1",
                options = colors
            )

            DressMeDropdown(
                value = color2,
                onValueChange = { color2 = it },
                label = "Couleur 2",
                options = colors
            )

            PrimaryButton(
                text = "Ajouter la règle de couleurs",
                onClick = {
                    if (color1.isNotBlank() && color2.isNotBlank() && color1 != color2) {
                        onAdd(color1, color2)
                    }
                },
                enabled = color1.isNotBlank() && color2.isNotBlank() && color1 != color2,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AddClothesRuleFormSection(
    clothes: List<Clothes>,
    onAdd: (Int, Int) -> Unit
) {
    val clothesOptions = remember(clothes) {
        clothes.map { cloth ->
            ClothesOption(
                id = cloth.id,
                label = when {
                    !cloth.nom.isNullOrBlank() -> "${cloth.nom} - ${cloth.color}"
                    !cloth.subCategory.isNullOrBlank() -> "${cloth.category} - ${cloth.subCategory} - ${cloth.color}"
                    else -> "${cloth.category} - ${cloth.color}"
                },
                imageUrl = cloth.imageUri
            )
        }
    }

    var selected1 by remember { mutableStateOf(clothesOptions.firstOrNull()) }
    var selected2 by remember { mutableStateOf(clothesOptions.getOrElse(1) { clothesOptions.firstOrNull() }) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerLarge),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimensions.elevationSmall
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacing16),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
        ) {
            Text(
                "Règles de vêtements",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Vêtement 1
            ClothesDropdownSection(
                label = "Vêtement 1",
                options = clothesOptions,
                selected = selected1,
                onSelect = { selected1 = it }
            )

            // Vêtement 2
            ClothesDropdownSection(
                label = "Vêtement 2",
                options = clothesOptions,
                selected = selected2,
                onSelect = { selected2 = it }
            )

            PrimaryButton(
                text = "Ajouter la règle de vêtements",
                onClick = {
                    val id1 = selected1?.id
                    val id2 = selected2?.id
                    if (id1 != null && id2 != null && id1 != id2) {
                        onAdd(id1, id2)
                    }
                },
                enabled = clothesOptions.size >= 2 && selected1?.id != selected2?.id,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ClothesDropdownSection(
    label: String,
    options: List<ClothesOption>,
    selected: ClothesOption?,
    onSelect: (ClothesOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = Dimensions.spacing8)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.spacing56)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerMedium)
                )
                .clickable { expanded = true },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.spacing12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
            ) {
                selected?.imageUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier.size(Dimensions.spacing40)
                    )
                }

                Text(
                    text = selected?.label.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
                            ) {
                                AsyncImage(
                                    model = option.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(Dimensions.spacing40)
                                )
                                Text(option.label)
                            }
                        },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

data class ClothesOption(
    val id: Int,
    val label: String,
    val imageUrl: String?
)
