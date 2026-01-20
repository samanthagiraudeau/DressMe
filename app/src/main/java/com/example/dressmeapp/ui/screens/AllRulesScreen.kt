package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

@Composable
fun AllRulesScreen(
    padding: PaddingValues,
    viewModel: RulesViewModel,
    clothesViewModel: ClothesViewModel
) {
    val colorRules by viewModel.colorRules.observeAsState(emptyList())
    val clothesRules by viewModel.clothesRules.observeAsState(emptyList())
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre principal
        item { PageTitle("Toutes les règles") }

        // Section : Règles de couleurs
        item { Spacer(Modifier.height(8.dp)) }
        item {
            Text(
                "Ne pas associer les couleurs",
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(colorRules) { rule ->
            RuleRow(
                rule = rule,
                onDelete = { viewModel.deleteRule(rule) }
            )
        }

        // Section : Règles de vêtements
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Text(
                "Ne pas associer les vêtements",
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(clothesRules) { rule ->
            RuleRowClothe(
                rule = rule,
                allClothes = allClothes,
                onDelete = { viewModel.deleteRule(rule) }
            )
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun RuleRow(
    rule: Rule,
    onDelete: (Rule) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${rule.first.lowercase()} et ${rule.second.lowercase()}",
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = { onDelete(rule) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun RuleRowClothe(
    rule: Rule,
    allClothes: List<Clothes>,
    onDelete: (Rule) -> Unit
) {
    val clothe1 = allClothes.find { it.id.toString() == rule.first }
    val clothe2 = allClothes.find { it.id.toString() == rule.second }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image + texte
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = clothe1?.imageUri,
                    contentDescription = clothe1?.nom,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("et", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(8.dp))
                AsyncImage(
                    model = clothe2?.imageUri,
                    contentDescription = clothe2?.nom,
                    modifier = Modifier.size(120.dp)
                )
            }

            // Bouton supprimer
            IconButton(onClick = { onDelete(rule) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
