import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.ColorEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.enums.MotifEnum
import com.example.dressmeapp.enums.SaisonEnum
import com.example.dressmeapp.ui.components.DressMeChipGroup
import com.example.dressmeapp.ui.components.DressMeDropdown
import com.example.dressmeapp.ui.components.DressMeTextField
import com.example.dressmeapp.ui.components.PrimaryButton
import com.example.dressmeapp.ui.theme.Dimensions
import com.example.dressmeapp.viewmodel.ClothesViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddClothesScreen(
    padding: PaddingValues,
    viewModel: ClothesViewModel,
    onBack: () -> Unit
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
    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Ajouter un vêtement") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Dimensions.spacing16)
        ) {
            // Image preview
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.imageHeightMedium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
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
                        Text(
                            "Aucune image sélectionnée",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Image selection button
            item {
                PrimaryButton(
                    text = "Choisir une photo",
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Name field
            item {
                DressMeTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = "Label (optionnel)"
                )
            }

            // Category dropdown
            item {
                DressMeDropdown(
                    value = category,
                    onValueChange = {
                        category = it
                        subCategory = null
                    },
                    label = "Catégorie",
                    options = CategoryEnum.entries.map { it.label }
                )
            }

            // SubCategory dropdown (conditional)
            if (category == CategoryEnum.HAUT.label || category == CategoryEnum.BAS.label) {
                val subCategories =
                    if (category == CategoryEnum.HAUT.label) subCategoriesHaut else subCategoriesBas
                item {
                    DressMeDropdown(
                        value = subCategory ?: "",
                        onValueChange = { subCategory = it },
                        label = "Sous-catégorie",
                        options = subCategories
                    )
                }
            }

            // Color dropdown
            item {
                DressMeDropdown(
                    value = color,
                    onValueChange = { color = it },
                    label = "Couleur",
                    options = ColorEnum.entries.map { it.label }
                )
            }

            // Pattern dropdown
            item {
                DressMeDropdown(
                    value = motif,
                    onValueChange = { motif = it },
                    label = "Motif",
                    options = MotifEnum.entries.map { it.label }
                )
            }

            // Seasons multi-select
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Saison",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = Dimensions.spacing8)
                    )
                    DressMeChipGroup(
                        items = SaisonEnum.entries.map { it.label },
                        selectedItems = selectedSeasons,
                        onItemChange = { item, isSelected ->
                            selectedSeasons = if (isSelected) {
                                selectedSeasons + item
                            } else {
                                selectedSeasons - item
                            }
                        }
                    )
                }
            }

            // Submit button
            item {
                PrimaryButton(
                    text = "Enregistrer",
                    onClick = {
                        val image = selectedImage ?: return@PrimaryButton

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
                            // Reset UI
                            selectedImage = null
                            category = ""
                            nom = ""
                            color = ""
                            motif = "Aucun"
                            subCategory = null
                            selectedSeasons = emptyList()
                        }
                        onBack()
                    },
                    enabled = selectedImage != null && category.isNotBlank() && color.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Bottom spacing
            item { Spacer(modifier = Modifier.height(Dimensions.spacing16)) }
        }
    }
}
