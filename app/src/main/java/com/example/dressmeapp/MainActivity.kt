
package com.example.dressmeapp

import AddClothesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.dressmeapp.ui.screens.AllClothesScreen
import com.example.dressmeapp.ui.screens.OutfitScreen
import com.example.dressmeapp.ui.theme.DressMeTheme
import com.example.dressmeapp.viewmodel.ClothesViewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.dressmeapp.ui.screens.AddOutfitScreen
import com.example.dressmeapp.ui.screens.AllRulesScreen
import com.example.dressmeapp.ui.screens.RulesScreen
import com.example.dressmeapp.ui.screens.TenuesScreen
import com.example.dressmeapp.viewmodel.OutfitViewModel
import com.example.dressmeapp.viewmodel.RulesViewModel

class MainActivity : ComponentActivity() {
    private val clothesVM: ClothesViewModel by viewModels()
    private val rulesVM: RulesViewModel by viewModels()
    private val outfitVM: OutfitViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DressMeTheme {
                var selectedTab by remember { mutableStateOf(0) }
                val colors = MaterialTheme.colorScheme
                var isAddingRule by remember { mutableStateOf(false) }
                var isAddingOutfit by remember { mutableStateOf(false) }
                var isAddingClothe by remember { mutableStateOf(false) }

            //    WindowCompat.setDecorFitsSystemWindows(window, false)

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar(
                                containerColor = colors.surface,       // fond de la barre
                                contentColor = colors.onSurface,       // couleur par défaut du contenu
                                tonalElevation = 3.dp,
                            ) {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    label = { Text("Dressing") },
                                    icon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.List,
                                            contentDescription = "Dressing"
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.onPrimary,
                                        selectedTextColor = colors.onPrimary,
                                        indicatorColor = colors.primary,
                                        unselectedIconColor = colors.onSurface.copy(alpha = 0.7f),
                                        unselectedTextColor = colors.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    label = { Text("Générer") },
                                    icon = {
                                        Icon(
                                            Icons.Default.AutoFixHigh,
                                            contentDescription = "Générer"
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.onPrimary,
                                        selectedTextColor = colors.onPrimary,
                                        indicatorColor = colors.primary,
                                        unselectedIconColor = colors.onSurface.copy(alpha = 0.7f),
                                        unselectedTextColor = colors.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 2,
                                    onClick = { selectedTab = 2 },
                                    label = { Text("Règles") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Straighten,
                                            contentDescription = "Règles"
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.onPrimary,
                                        selectedTextColor = colors.onPrimary,
                                        indicatorColor = colors.primary,
                                        unselectedIconColor = colors.onSurface.copy(alpha = 0.7f),
                                        unselectedTextColor = colors.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 3,
                                    onClick = { selectedTab = 3 },
                                    label = { Text("Tenues") },
                                    icon = {
                                        Icon(
                                            Icons.Default.Checkroom,
                                            contentDescription = "Tenues"
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.onPrimary,
                                        selectedTextColor = colors.onPrimary,
                                        indicatorColor = colors.primary,
                                        unselectedIconColor = colors.onSurface.copy(alpha = 0.7f),
                                        unselectedTextColor = colors.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                            }
                        }
                    ) { padding ->
                            when (selectedTab) {
                                0 -> {
                                    if (isAddingClothe) {
                                        AddClothesScreen(
                                            padding = padding,
                                            viewModel = clothesVM,
                                            onBack = { isAddingClothe = false })
                                    } else {
                                        AllClothesScreen(
                                            padding = padding,
                                            viewModel = clothesVM,
                                            onAddClotheClick = { isAddingClothe = true })
                                    }
                                }

                                1 -> OutfitScreen(
                                    padding = padding,
                                    viewModel = clothesVM,
                                    rulesViewModel = rulesVM,
                                    outfitViewModel = outfitVM
                                )

                                2 -> {
                                    if (isAddingRule) {
                                        RulesScreen(
                                            padding = padding,
                                            viewModel = rulesVM,
                                            clothesViewModel = clothesVM,
                                            onBack = { isAddingRule = false }
                                        )
                                    } else {
                                        AllRulesScreen(
                                            padding = padding,
                                            viewModel = rulesVM,
                                            clothesViewModel = clothesVM,
                                            onAddRuleClick = { isAddingRule = true }
                                        )
                                    }
                                }

                                3 -> {
                                    if (isAddingOutfit) {
                                        AddOutfitScreen(
                                            padding = padding,
                                            outfitsVM = outfitVM,
                                            clothesViewModel = clothesVM,
                                            onBack = { isAddingOutfit = false }
                                        )
                                    } else {
                                        TenuesScreen(
                                            padding = padding,
                                            clothesViewModel = clothesVM,
                                            outfitsVM = outfitVM,
                                            onAddOutfitClick = { isAddingOutfit = true })
                                    }
                                }
                            }
                        }
                    }

            }
        }
    }
}




