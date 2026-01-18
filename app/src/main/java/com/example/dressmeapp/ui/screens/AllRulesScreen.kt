package com.example.dressmeapp.ui.screens

import androidx.compose.runtime.livedata.observeAsState
import com.example.dressmeapp.ui.components.PageTitle
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
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

@Composable
fun AllRulesScreen(padding: PaddingValues,
                viewModel: RulesViewModel, clothesViewModel: ClothesViewModel
) {

    val colorRules by viewModel.colorRules.observeAsState(emptyList())
    val clothesRules by viewModel.clothesRules.observeAsState(emptyList())
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())

    LazyColumn(Modifier.fillMaxSize().padding(padding).imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            PageTitle("Toutes les règles")
        }

        // --- Section Règles de couleurs ---
        item {
            Spacer(Modifier.height(14.dp))
            Text("Règles de couleurs", style = MaterialTheme.typography.titleMedium)
        }

        items(colorRules) { rule ->
            Spacer(Modifier.height(14.dp))
            RuleRow(rule = rule, onDelete = { viewModel.deleteRule(rule) })
        }



        // --- Section Règles de vêtements ---
        item {
            Spacer(Modifier.height(72.dp))
            Text("Règles de vêtements", style = MaterialTheme.typography.titleMedium)
        }

        items(clothesRules) { rule ->
            RuleRowClothe(rule = rule, onDelete = { viewModel.deleteRule(rule) }, allClothes)
        }
        item {
            Spacer(Modifier.height(14.dp))

        }

    }
}

@Composable
private fun RuleRowClothe(rule: Rule, onDelete: (Rule) -> Unit, allClothes: List<Clothes>) {
    val clothe1 = allClothes.find { it.id.toString() == rule.first }
    val clothe2 = allClothes.find { it.id.toString() == rule.second }
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val label1 = when {
                !clothe1?.nom.isNullOrBlank() ->
                    "${clothe1?.nom} - ${clothe1?.color}"
                !clothe1?.subCategory.isNullOrBlank() ->
                    "${clothe1?.category} - ${clothe1?.subCategory} - ${clothe1?.color}"
                else ->
                    "${clothe1?.category} - ${clothe1?.color}"
            }

            val label2 = when {
                !clothe2?.nom.isNullOrBlank() ->
                    "${clothe2?.nom} - ${clothe2?.color}"
                !clothe2?.subCategory.isNullOrBlank() ->
                    "${clothe2?.category} - ${clothe2?.subCategory} - ${clothe2?.color}"
                else ->
                    "${clothe2?.category} - ${clothe2?.color}"
            }
            Text("$label1 ❌ $label2", style = MaterialTheme.typography.bodyMedium)
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
private fun RuleRow(rule: Rule, onDelete: (Rule) -> Unit) {
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${rule.first} ❌ ${rule.second}", style = MaterialTheme.typography.bodyMedium)
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
