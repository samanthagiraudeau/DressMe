package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dressmeapp.enums.ColorEnum
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.ui.components.PageTitle
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Section : Règles de couleurs
            item {
                Text("Règles de couleurs", style = MaterialTheme.typography.titleMedium)
            }
            item {
                AddColorRuleForm(
                    colors = colorsList,
                    onAdd = { c1, c2 -> viewModel.addColorRule(c1, c2) },
                    onBack = { onBack() }
                )
            }

            // Section : Règles de vêtements
            item {
                Spacer(Modifier.height(12.dp))
                Text("Règles de vêtements", style = MaterialTheme.typography.titleMedium)
            }
            item {
                AddClothesRuleForm(
                    clothes = allClothes,
                    onAdd = { v1, v2 -> viewModel.addClothesRule(v1.toString(), v2.toString()) },
                    onBack = { onBack() }
                )
            }
        }
    }
}

@Composable
private fun AddColorRuleForm(
    colors: List<String>,
    onAdd: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var color1 by remember { mutableStateOf(colors.firstOrNull().orEmpty()) }
    var color2 by remember { mutableStateOf(colors.getOrElse(1) { colors.firstOrNull().orEmpty() }) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DropdownMenuBox(label = "Couleur 1", options = colors, selected = color1, onSelect = { color1 = it })
        DropdownMenuBox(label = "Couleur 2", options = colors, selected = color2, onSelect = { color2 = it })

        Button(
            onClick = {
                if (color1.isNotBlank() && color2.isNotBlank() && color1 != color2) onAdd(color1, color2)
                onBack()
                      },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ajouter la règle de couleurs") }
    }
}

@Composable
private fun AddClothesRuleForm(
    clothes: List<Clothes>,
    onAdd: (Int, Int) -> Unit,
    onBack: () -> Unit
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

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ClothesDropdownMenuBox(label = "Vêtement 1", options = clothesOptions, selected = selected1, onSelect = { selected1 = it })
        ClothesDropdownMenuBox(label = "Vêtement 2", options = clothesOptions, selected = selected2, onSelect = { selected2 = it })

        Button(
            onClick = {
                val id1 = selected1?.id
                val id2 = selected2?.id
                if (id1 != null && id2 != null && id1 != id2) onAdd(id1, id2)
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = clothesOptions.size >= 2
        ) { Text("Ajouter la règle de vêtements") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClothesDropdownMenuBox(
    label: String,
    options: List<ClothesOption>,
    selected: ClothesOption?,
    onSelect: (ClothesOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = selected?.label.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                selected?.imageUrl?.let { AsyncImage(model = it, contentDescription = null, modifier = Modifier.size(40.dp)) }
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = option.imageUrl, contentDescription = null, modifier = Modifier.size(60.dp).padding(end = 8.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

data class ClothesOption(
    val id: Int,
    val label: String,
    val imageUrl: String?
)
