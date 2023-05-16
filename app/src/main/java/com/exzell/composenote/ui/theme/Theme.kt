package com.exzell.composenote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val paletteDark = darkColors(
        primary = colorYellow,
        primaryVariant = colorYellowDark,
        secondary = colorOrange,
        background = colorSomehowBlack
)

private val paletteLight = lightColors(
        primary = colorYellow,
        primaryVariant = colorYellow,
        secondary = colorOrange

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeNoteTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) paletteDark else paletteLight

    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = Shapes,
            content = content
    )
}