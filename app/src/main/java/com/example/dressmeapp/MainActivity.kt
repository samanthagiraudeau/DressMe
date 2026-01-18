
package com.example.dressmeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.dressmeapp.ui.screens.AddClothesScreen
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
import com.example.dressmeapp.ui.screens.AllRulesScreen
import com.example.dressmeapp.ui.screens.RulesScreen
import com.example.dressmeapp.viewmodel.RulesViewModel

class MainActivity : ComponentActivity() {
    private val clothesVM: ClothesViewModel by viewModels()
    private val rulesVM: RulesViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DressMeTheme {
                var selectedTab by remember { mutableStateOf(0) }
                val colors = MaterialTheme.colorScheme

                WindowCompat.setDecorFitsSystemWindows(window, false)

                Box(modifier = Modifier.fillMaxSize()){
                    Scaffold(
                        bottomBar = {
                            NavigationBar(
                                containerColor = colors.surface,       // fond de la barre
                                contentColor = colors.onSurface,       // couleur par défaut du contenu
                                tonalElevation = 3.dp
                            ) {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    label = { Text("Ajouter") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Add,
                                            contentDescription = "Ajouter"
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.onPrimary,
                                        selectedTextColor = colors.onPrimary,
                                        indicatorColor = colors.primary, // pastille/indicateur sous l’item sélectionné
                                        unselectedIconColor = colors.onSurface.copy(alpha = 0.7f),
                                        unselectedTextColor = colors.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
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
                                    selected = selectedTab == 2,
                                    onClick = { selectedTab = 2 },
                                    label = { Text("Tenue") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Style,
                                            contentDescription = "Tenue"
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
                                    label = { Text("+ règles") },
                                    icon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Rule,
                                            contentDescription = "+ règles"
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
                                    selected = selectedTab == 4,
                                    onClick = { selectedTab = 4 },
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
                            }
                        }
                    ) { padding ->
                        when (selectedTab) {
                            0 -> AddClothesScreen(padding = padding, viewModel = clothesVM)
                            1 -> AllClothesScreen(padding = padding, viewModel = clothesVM)
                            2 -> OutfitScreen(
                                padding = padding,
                                viewModel = clothesVM,
                                rulesViewModel = rulesVM
                            )

                            3 -> RulesScreen(
                                padding = padding,
                                viewModel = rulesVM,
                                clothesViewModel = clothesVM
                            )

                            4 -> AllRulesScreen(padding = padding, viewModel = rulesVM, clothesViewModel = clothesVM)
                        }
                    }

                }
            }
        }
    }
}


@Composable
private fun BrandSplash() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C)), // gris foncé
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Dress Me",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



