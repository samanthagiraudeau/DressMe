@file:Suppress("unused", "UNUSED_VARIABLE", "UNUSED")

package com.example.dressmeapp.ui.theme

import androidx.compose.ui.graphics.Color

// Legacy colors (kept for compatibility)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// DressMe Brand Colors - Palette identitaire
internal val DressMe_Bordeaux = Color(0xFF6B1F2B)
internal val DressMe_VertSapin = Color(0xFF1F3D2B)
internal val DressMe_MarronChaud = Color(0xFF5A3A2E)

internal val DressMe_LieDeVin = Color(0xFF7B2D3A)
internal val DressMe_Prune = Color(0xFF5B3A5D)
internal val DressMe_BleuElectrique = Color(0xFF2E5FA7)

internal val DressMe_Framboise = Color(0xFFC05A78)
internal val DressMe_Corail = Color(0xFFD66A4A)
internal val DressMe_VertLumineux = Color(0xFF3F8F5A)

internal val DressMe_Ecru = Color(0xFFF1E8DC)
internal val DressMe_BeigeRose = Color(0xFFD8C2B5)
internal val DressMe_BrunFonce = Color(0xFF2F2623)
internal val DressMe_GrisChaud = Color(0xFF6E625C)

internal val DressMe_PresqueNoir = Color(0xFF1A1615)

// Alias pour compatibilite avec Theme.kt
val DressMe_Primary = DressMe_Bordeaux
val DressMe_PrimaryDark = DressMe_LieDeVin
val DressMe_PrimaryLight = DressMe_Framboise

val DressMe_Secondary = DressMe_VertSapin
val DressMe_SecondaryDark = DressMe_BrunFonce
val DressMe_SecondaryLight = DressMe_VertLumineux

val DressMe_Tertiary = DressMe_MarronChaud

val DressMe_Background = DressMe_Ecru
val DressMe_Surface = DressMe_BeigeRose
val DressMe_SurfaceVariant = DressMe_Ecru

val DressMe_OnBackground = DressMe_BrunFonce
val DressMe_OnSurface = DressMe_BrunFonce

val DressMe_Error = DressMe_Corail
val DressMe_Success = DressMe_VertLumineux
val DressMe_Warning = DressMe_Framboise

val DressMe_Outline = DressMe_GrisChaud

@Suppress("unused")
internal val DressMe_Palette = listOf(
    DressMe_Bordeaux,
    DressMe_VertSapin,
    DressMe_MarronChaud,
    DressMe_LieDeVin,
    DressMe_Prune,
    DressMe_BleuElectrique,
    DressMe_Framboise,
    DressMe_Corail,
    DressMe_VertLumineux,
    DressMe_Ecru,
    DressMe_BeigeRose,
    DressMe_BrunFonce,
    DressMe_GrisChaud,
    DressMe_PresqueNoir,
    DressMe_Primary,
    DressMe_PrimaryDark,
    DressMe_PrimaryLight,
    DressMe_Secondary,
    DressMe_SecondaryDark,
    DressMe_SecondaryLight,
    DressMe_Tertiary,
    DressMe_Background,
    DressMe_Surface,
    DressMe_SurfaceVariant,
    DressMe_OnBackground,
    DressMe_OnSurface,
    DressMe_Error,
    DressMe_Success,
    DressMe_Warning,
    DressMe_Outline
)
