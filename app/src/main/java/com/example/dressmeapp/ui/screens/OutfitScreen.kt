
package com.example.dressmeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dressmeapp.ui.components.PageTitle
import com.example.dressmeapp.viewmodel.ClothesViewModel
import kotlinx.coroutines.launch
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.ui.draw.scale
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.viewmodel.RulesViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OutfitScreen(padding: PaddingValues, viewModel: ClothesViewModel, rulesViewModel: RulesViewModel) {
    val scope = rememberCoroutineScope()
    var outfit by remember { mutableStateOf<com.example.dressmeapp.viewmodel.Outfit?>(null) }

    var generatGlobalOutfit by remember {mutableStateOf(false)}
    var generatGiletWithTeeShirt by remember {mutableStateOf(true)}
    val allRules by rulesViewModel.allRules.observeAsState(emptyList());

    LaunchedEffect(Unit) {
        outfit = viewModel.getRandomOutfit(generatGlobalOutfit, generatGiletWithTeeShirt, allRules)
    }

    Column (
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PageTitle("Tenue aléatoire")
            }

            item {
                FlowRow(
                    mainAxisSpacing = 16.dp,   // Espacement horizontal entre les slots
                    crossAxisSpacing = 16.dp,
                ) {
                    OutfitSlot(title = CategoryEnum.MANTEAU.label, uri = outfit?.manteau?.imageUri)

                    if(generatGlobalOutfit) {
                        OutfitSlot(title = CategoryEnum.GLOBAL.label, uri = outfit?.global?.imageUri)
                    } else {
                        OutfitSlot(title = CategoryEnum.HAUT.label, uri = outfit?.haut?.imageUri)
                        OutfitSlot(title = CategoryEnum.BAS.label, uri = outfit?.bas?.imageUri)
                        if(generatGiletWithTeeShirt && outfit?.teeShirt != null) {
                            OutfitSlot(title = SubCategoryEnum.TEESHIRT.label, uri = outfit?.teeShirt?.imageUri)
                        }
                    }

                    OutfitSlot(title = CategoryEnum.CHAUSSURES.label, uri = outfit?.chaussures?.imageUri)
                }
            }
        }

        // Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Robe ou combi")
            androidx.compose.material3.Switch(
                checked = generatGlobalOutfit,
                onCheckedChange = { generatGlobalOutfit = it } ,
                modifier = Modifier.scale(0.7f)
            )

            Spacer(modifier = Modifier.padding(2.dp, 0.dp))

            Text("T-shirt sous gilet")
            androidx.compose.material3.Switch(
                checked = generatGiletWithTeeShirt,
                onCheckedChange = { generatGiletWithTeeShirt = it },
                modifier = Modifier.scale(0.7f)
            )
        }

        // Toggle
      /*  Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Porter un tee-shirt avec les gilets ?")
            androidx.compose.material3.Switch(
                checked = generatGiletWithTeeShirt,
                onCheckedChange = { generatGiletWithTeeShirt = it },
                modifier = Modifier.scale(0.7f)
            )
        }*/

        Button(onClick = { scope.launch { outfit = viewModel.getRandomOutfit(generatGlobalOutfit, generatGiletWithTeeShirt, allRules) } }) {
            Text("Générer une autre tenue")
        }

        Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 2.dp))

    }
    /*
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PageTitle("Tenue aléatoire")
        }

        item {
            FlowRow(
                mainAxisSpacing = 16.dp,   // Espacement horizontal entre les slots
                crossAxisSpacing = 16.dp,
            ) {
                OutfitSlot(title = CategoryEnum.MANTEAU.label, uri = outfit?.manteau?.imageUri)

                if(generatGlobalOutfit) {
                    OutfitSlot(title = CategoryEnum.GLOBAL.label, uri = outfit?.global?.imageUri)
                } else {
                    OutfitSlot(title = CategoryEnum.HAUT.label, uri = outfit?.haut?.imageUri)
                    OutfitSlot(title = CategoryEnum.BAS.label, uri = outfit?.bas?.imageUri)
                    if(generatGiletWithTeeShirt && outfit?.teeShirt != null) {
                        OutfitSlot(title = SubCategoryEnum.TEESHIRT.label, uri = outfit?.teeShirt?.imageUri)
                    }
                }

                OutfitSlot(title = CategoryEnum.CHAUSSURES.label, uri = outfit?.chaussures?.imageUri)
            }
        }
        item {
            // Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Tenue avec robe ou combi ?")
                androidx.compose.material3.Switch(
                    checked = generatGlobalOutfit,
                    onCheckedChange = { generatGlobalOutfit = it } ,
                    modifier = Modifier.scale(0.7f)
                )
            }
        }
        item {
            // Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Porter un tee-shirt avec les gilets ?")
                androidx.compose.material3.Switch(
                    checked = generatGiletWithTeeShirt,
                    onCheckedChange = { generatGiletWithTeeShirt = it },
                    modifier = Modifier.scale(0.7f)
                )
            }
        }
        item {
            Button(onClick = { scope.launch { outfit = viewModel.getRandomOutfit(generatGlobalOutfit, generatGiletWithTeeShirt, allRules) } }) {
                Text("Générer une autre tenue")
            }

            /* if (outfit?.manteau == null || outfit?.haut == null || outfit?.bas == null || outfit?.chaussures == null) {
                Text("Ajoute plus de vêtements !")
            } */
        }


    }

     */
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
