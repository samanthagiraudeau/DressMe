package com.example.dressmeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val DressMeTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

private val DressMeLightColorScheme = lightColorScheme(
    primary = DressMe_Light_Bordeaux,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = DressMe_Light_LieDeVin,
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = DressMe_Light_VertSapin,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = DressMe_Light_MarronChaud,
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = DressMe_Light_MarronChaud,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = DressMe_Light_Prune,
    onTertiaryContainer = Color(0xFFFFFFFF),
    error = DressMe_Light_Corail,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = DressMe_Light_Corail,
    background = DressMe_Light_Background,
    onBackground = DressMe_Light_OnBackground,
    surface = DressMe_Light_Surface,
    onSurface = DressMe_Light_OnSurface,
    surfaceVariant = DressMe_Light_SurfaceVariant,
    onSurfaceVariant = Color(0xFF4E4642),
    outline = DressMe_Light_Outline,
    outlineVariant = Color(0xFFCEC4BE),
    scrim = Color(0xFF000000)
)

private val DressMeDarkColorScheme = darkColorScheme(
    primary = DressMe_Dark_Bordeaux,
    onPrimary = Color(0xFFFFFBF7),
    primaryContainer = DressMe_Dark_LieDeVin,
    onPrimaryContainer = DressMe_Ecru,
    secondary = DressMe_Dark_VertSapin,
    onSecondary = Color(0xFFFFFBF7),
    secondaryContainer = Color(0xFF3F4D3B),
    onSecondaryContainer = DressMe_Ecru,
    tertiary = DressMe_Dark_MarronChaud,
    onTertiary = Color(0xFFFFFBF7),
    tertiaryContainer = DressMe_Dark_Prune,
    onTertiaryContainer = DressMe_Ecru,
    error = DressMe_Dark_Corail,
    onError = Color(0xFFFFFBF7),
    errorContainer = Color(0xFF8B2D2A),
    onErrorContainer = DressMe_Ecru,
    background = DressMe_Dark_Background,
    onBackground = DressMe_Dark_OnBackground,
    surface = DressMe_Dark_Surface,
    onSurface = DressMe_Dark_OnSurface,
    surfaceVariant = DressMe_Dark_SurfaceVariant,
    onSurfaceVariant = Color(0xFFD0C6C0),
    outline = DressMe_Dark_Outline,
    outlineVariant = Color(0xFF5A4E48),
    scrim = Color(0xFF000000),
)

@Composable
fun DressMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DressMeDarkColorScheme
    } else {
        DressMeLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DressMeTypography,
        content = content
    )
}