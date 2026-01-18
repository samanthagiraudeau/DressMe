package com.example.dressmeapp.ui.screens

import androidx.compose.runtime.livedata.observeAsState
import com.example.dressmeapp.ui.components.PageTitle




import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

@Composable
fun RulesScreen(padding: PaddingValues,
    viewModel: RulesViewModel, clothesViewModel: ClothesViewModel
  //  allClothes: List<Clothes>, // Liste des vêtements pour les menus
    //colorsList: List<String> = listOf("Noir", "Blanc", "Bleu", "Rouge", "Vert", "Jaune", "Gris", "Rose", "Beige")
) {
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())
    val colorsList: List<String> = listOf("Vert", "Bleu", "Rouge", "Noir", "Orange", "Bordeaux", "Marron", "Blanc", "Beige", "Écru", "Multicolore")
    val colorRules by viewModel.colorRules.observeAsState(emptyList())
    val clothesRules by viewModel.clothesRules.observeAsState(emptyList())

    LazyColumn( modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            PageTitle("Gestion des règles")
        }

        // --- Section Règles de couleurs ---
        item {
            Text("Règles de couleurs", style = MaterialTheme.typography.titleMedium)
        }
        item {
            AddColorRuleForm(
                colors = colorsList,
                onAdd = { c1, c2 -> viewModel.addColorRule(c1, c2) }
            )
        }


        // --- Section Règles de vêtements ---
        item {
            Spacer(Modifier.height(14.dp))
            Text("Règles de vêtements", style = MaterialTheme.typography.titleMedium)
        }
        item {
            AddClothesRuleForm(
                clothes = allClothes,
                onAdd = { v1, v2 -> viewModel.addClothesRule(v1.toString(), v2.toString()) }
            )
        }
    }
}


@Composable
private fun AddColorRuleForm(
    colors: List<String>,
    onAdd: (String, String) -> Unit
) {
    var color1 by remember { mutableStateOf(colors.firstOrNull().orEmpty()) }
    var color2 by remember { mutableStateOf(colors.getOrElse(1) { colors.firstOrNull().orEmpty() }) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DropdownMenuBox(
            label = "Couleur 1",
            options = colors,
            selected = color1,
            onSelect = { color1 = it },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuBox(
            label = "Couleur 2",
            options = colors,
            selected = color2,
            onSelect = { color2 = it },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { if (color1.isNotBlank() && color2.isNotBlank() && color1 != color2) onAdd(color1, color2) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ajouter la règle de couleurs") }
    }
}

@Composable
private fun AddClothesRuleForm(
    clothes: List<Clothes>,
    onAdd: (Int, Int) -> Unit
) {
    // Labels lisibles pour l’utilisateur
    val clothesOptions = remember(clothes) {
        clothes.map { cloth ->
            val label = when {
                !cloth.nom.isNullOrBlank() -> "${cloth.nom} - ${cloth.color}"
                !cloth.subCategory.isNullOrBlank() -> "${cloth.category} - ${cloth.subCategory} - ${cloth.color}"
                else -> "${cloth.category} - ${cloth.color}"
            }
            label to cloth.id
        }
    }

    // Sélections initiales sécurisées
    var selected1 by remember { mutableStateOf(clothesOptions.firstOrNull()) }
    var selected2 by remember { mutableStateOf(clothesOptions.getOrElse(1) { clothesOptions.firstOrNull() }) }

    LaunchedEffect(clothesOptions) {
        if (selected1 == null && clothesOptions.isNotEmpty()) selected1 = clothesOptions.first()
        if (selected2 == null && clothesOptions.size > 1) selected2 = clothesOptions[1]
    }


    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DropdownMenuBox(
            label = "Vêtement 1",
            options = clothesOptions.map { it.first },
            selected = selected1?.first.orEmpty(),
            onSelect = { label ->
                selected1 = clothesOptions.find { it.first == label }
                       },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuBox(
            label = "Vêtement 2",
            options = clothesOptions.map { it.first },
            selected = selected2?.first.orEmpty(),
            onSelect = { label ->
                selected2 = clothesOptions.find { it.first == label }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val id1 = selected1?.second
                val id2 = selected2?.second
                if (id1 != null && id2 != null && id1 != id2) {
                    onAdd(id1, id2)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = clothesOptions.size >= 2
        ) {
            Text("Ajouter la règle de vêtements")
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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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

