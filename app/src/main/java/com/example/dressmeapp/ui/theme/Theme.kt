package com.example.dressmeapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
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

private val DressMeDarkColorScheme = darkColorScheme(
    primary = DressMe_Primary,
    onPrimary = DressMe_Ecru,
    primaryContainer = DressMe_PrimaryDark,
    onPrimaryContainer = DressMe_Ecru,
    secondary = DressMe_Secondary,
    onSecondary = DressMe_Ecru,
    secondaryContainer = DressMe_SecondaryDark,
    onSecondaryContainer = DressMe_Ecru,
    tertiary = DressMe_Tertiary,
    onTertiary = DressMe_Ecru,
    tertiaryContainer = DressMe_Prune,
    onTertiaryContainer = DressMe_Ecru,
    error = DressMe_Error,
    onError = DressMe_Ecru,
    errorContainer = DressMe_LieDeVin,
    onErrorContainer = DressMe_Ecru,
    background = DressMe_Background,
    onBackground = DressMe_OnBackground,
    surface = DressMe_Surface,
    onSurface = DressMe_OnSurface,
    surfaceVariant = DressMe_SurfaceVariant,
    onSurfaceVariant = DressMe_GrisChaud,
    outline = DressMe_Outline,
    outlineVariant = DressMe_GrisChaud,
    scrim = DressMe_PresqueNoir
)

@Composable
fun DressMeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DressMeDarkColorScheme,
        typography = DressMeTypography,
        content = content
    )
}