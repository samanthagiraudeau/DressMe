
package com.example.dressmeapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.ColorEnum
import com.example.dressmeapp.enums.MotifEnum
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.utils.copyImageToInternalStorage
import com.example.dressmeapp.viewmodel.ClothesViewModel

@Composable
fun AddClothesScreen(padding: PaddingValues, viewModel: ClothesViewModel) {
    val context = LocalContext.current

    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var category by remember { mutableStateOf("") }
    var nom by remember { mutableStateOf("") }
    var subCategory by remember { mutableStateOf<String?>(null) }
    var color by remember { mutableStateOf("") }
    var motif by remember { mutableStateOf("Aucun") }
    var season by remember { mutableStateOf("Toutes") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImage = uri
    }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .imePadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PageTitle("Ajouter un vêtement")
        }
        item {
            // Aperçu image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImage != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Aucune image sélectionnée")
                }
            }
        }
        item {
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choisir une photo")
            }
        }
        item {
            TextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Label") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            val categories = CategoryEnum.entries.map { it.label }
            val subCategoriesHaut = listOf(SubCategoryEnum.PULL.label, SubCategoryEnum.GILET.label, SubCategoryEnum.TEESHIRT.label)
            val subCategoriesBas = listOf(SubCategoryEnum.PANTALON.label, SubCategoryEnum.JUPE.label, SubCategoryEnum.SHORT.label)

            // Category (menu simple)
            ExposedDropdownMenuBoxSample(
                label = "Catégorie",
                options = categories,
                value = category,
                onValueChange = {
                    category = it
                    subCategory = null
                }
            )

            if (category == "Haut") {
                ExposedDropdownMenuBoxSample(
                    label = "Sous-catégorie",
                    options = subCategoriesHaut,
                    value = subCategory ?: "",
                    onValueChange = { subCategory = it }
                )
            } else if (category == "Bas") {
                ExposedDropdownMenuBoxSample(
                    label = "Sous-catégorie",
                    options = subCategoriesBas,
                    value = subCategory ?: "",
                    onValueChange = { subCategory = it }
                )
            }}
        item {
            ExposedDropdownMenuBoxSample(
                label = "Couleur",
                options = ColorEnum.entries.map { it.label },
                value = color,
                onValueChange = { color = it }
            )
        }
        item {
            ExposedDropdownMenuBoxSample(
                label = "Motif",
                options = MotifEnum.entries.map {it.label},
                value = motif,
                onValueChange = { motif = it }
            )
        }
        item {
            // Saison (menu simple)
            ExposedDropdownMenuBoxSample(
                label = "Saison",
                options = SaisonEnum.entries.map {it.label},
                value = season,
                onValueChange = { season = it }
            )
        }
        item {
            Button(
                onClick = {
                    selectedImage?.let { sourceUri ->
                        // 1) Copie dans le stockage interne (JPEG 90%)
                        val localPath = copyImageToInternalStorage(context, sourceUri)

                        // 2) Sauvegarde le chemin absolu en base
                        viewModel.addClothes(
                            category = category,
                            subCategory = subCategory,
                            color = color.trim(),
                            season = season,
                            imageUri = localPath,
                            motif = motif,
                            nom = nom
                        )

                        // 3) Reset
                        selectedImage = null
                    }
                },
                enabled = selectedImage != null && color.isNotBlank(),
                modifier = Modifier.fillMaxWidth()

            ) {
                Text("Enregistrer")
            }
        }
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
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = value,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
