package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.ui.components.AddFab
import com.example.dressmeapp.ui.components.ExpandableSection
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

@Composable
fun AllRulesScreen(
    padding: PaddingValues,
    viewModel: RulesViewModel,
    clothesViewModel: ClothesViewModel,
    onAddRuleClick: () -> Unit
) {
    val colorRules by viewModel.colorRules.observeAsState(emptyList())
    val clothesRules by viewModel.clothesRules.observeAsState(emptyList())
    val allClothes by clothesViewModel.allClothes.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Titre principal
            item { PageTitle("Toutes les règles") }

            // Section : Règles de couleurs
            if (colorRules.isNotEmpty()) {
                item {
                    ExpandableSection(
                        title = "Associer les couleurs (${colorRules.size})",
                        initiallyExpanded = true
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
                        ) {
                            colorRules.forEach { rule ->
                                RuleColorCard(
                                    rule = rule,
                                    onDelete = { viewModel.deleteRule(rule) }
                                )
                            }
                        }
                    }
                }
            }

            // Section : Règles de vêtements
            if (clothesRules.isNotEmpty()) {
                item {
                    ExpandableSection(
                        title = "Associer les vêtements (${clothesRules.size})",
                        initiallyExpanded = true
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
                        ) {
                            clothesRules.forEach { rule ->
                                RuleClothesCard(
                                    rule = rule,
                                    allClothes = allClothes,
                                    onDelete = { viewModel.deleteRule(rule) }
                                )
                            }
                        }
                    }
                }
            }

            // Espace en bas pour FAB
            item { Spacer(modifier = Modifier.height(Dimensions.spacing32)) }
        }

        AddFab(
            onClick = onAddRuleClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimensions.spacing16),
            contentDescription = "Ajouter une règle"
        )
    }
}

@Composable
private fun RuleColorCard(
    rule: Rule,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerMedium),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimensions.elevationSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${rule.first.lowercase()} et ${rule.second.lowercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun RuleClothesCard(
    rule: Rule,
    allClothes: List<Clothes>,
    onDelete: () -> Unit
) {
    val clothe1 = allClothes.find { it.id.toString() == rule.first }
    val clothe2 = allClothes.find { it.id.toString() == rule.second }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimensions.cornerMedium),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimensions.elevationSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Images + texte
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
            ) {
                AsyncImage(
                    model = clothe1?.imageUri,
                    contentDescription = clothe1?.nom,
                    modifier = Modifier.size(Dimensions.spacing80)
                )

                Text(
                    "et",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AsyncImage(
                    model = clothe2?.imageUri,
                    contentDescription = clothe2?.nom,
                    modifier = Modifier.size(Dimensions.spacing80)
                )
            }

            // Bouton supprimer
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
