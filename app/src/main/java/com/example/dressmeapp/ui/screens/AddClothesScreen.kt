import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.ColorEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.enums.MotifEnum
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddClothesScreen(
    padding: PaddingValues,
    viewModel: ClothesViewModel
) {
    val context = LocalContext.current

    var selectedImage by rememberSaveable { mutableStateOf<Uri?>(null) }
    var category by rememberSaveable { mutableStateOf("") }
    var nom by rememberSaveable { mutableStateOf("") }
    var subCategory by rememberSaveable { mutableStateOf<String?>(null) }
    var color by rememberSaveable { mutableStateOf("") }
    var motif by rememberSaveable { mutableStateOf("Aucun") }
    var selectedSeasons by rememberSaveable { mutableStateOf(listOf<String>()) }


    val subCategoriesHaut = listOf(
        SubCategoryEnum.PULL.label,
        SubCategoryEnum.GILET.label,
        SubCategoryEnum.TEESHIRT.label
    )

    val subCategoriesBas = listOf(
        SubCategoryEnum.PANTALON.label,
        SubCategoryEnum.JUPE.label,
        SubCategoryEnum.SHORT.label
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
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

        item { PageTitle("Ajouter un vêtement") }

        item {
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
            ExposedDropdownMenuBoxSample(
                label = "Catégorie",
                options = CategoryEnum.entries.map { it.label },
                value = category,
                onValueChange = {
                    category = it
                    subCategory = null
                }
            )
        }

        if (category == CategoryEnum.HAUT.label || category == CategoryEnum.BAS.label) {
            val subCategories = if (category == CategoryEnum.HAUT.label) subCategoriesHaut else subCategoriesBas
            item {
                ExposedDropdownMenuBoxSample(
                    label = "Sous-catégorie",
                    options = subCategories,
                    value = subCategory ?: "",
                    onValueChange = { subCategory = it }
                )
            }
        }

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
                options = MotifEnum.entries.map { it.label },
                value = motif,
                onValueChange = { motif = it }
            )
        }

        item {
            MultiSelectDropdownMenu(
                label = "Saison",
                options = SaisonEnum.entries.map { it.label },
                selectedItems = selectedSeasons,
                onSelectionChange = { selectedSeasons = it }
            )
        }

        item {
            Button(
                enabled = selectedImage != null && category.isNotBlank() && color.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val image = selectedImage ?: return@Button

                    viewModel.saveClothes(
                        context = context,
                        sourceUri = image,
                        category = category,
                        subCategory = subCategory,
                        color = color,
                        seasons = selectedSeasons,
                        motif = motif,
                        nom = nom
                    ) {
                        // reset UI
                        selectedImage = null
                        category = ""
                        nom = ""
                        color = ""
                        motif = "Aucun"
                        subCategory = null
                        selectedSeasons = emptyList()
                    }
                }
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectDropdownMenu(
    label: String,
    options: List<String>,
    selectedItems: List<String>,
    onSelectionChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {

        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = if (selectedItems.isEmpty()) "" else selectedItems.joinToString(", "),
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
                    trailingIcon = {
                        if (selectedItems.contains(option)) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    },
                    onClick = {
                        val newList = if (selectedItems.contains(option)) {
                            selectedItems - option
                        } else {
                            selectedItems + option
                        }
                        onSelectionChange(newList)
                    }
                )
            }
        }
    }
}

