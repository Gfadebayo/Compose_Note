package com.exzell.composenote.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.exzell.composenote.R


val familyMontserrat = FontFamily(
        Font(R.font.montserrat_medium_500, weight = FontWeight.Medium),
        Font(R.font.montserrat_regular_400),
        Font(R.font.montserrat_bold_700, weight = FontWeight.Bold)
)

val textStyleDefault = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = familyMontserrat,
        lineHeight = 16.sp
)



val typography = Typography(
        defaultFontFamily = familyMontserrat,
)