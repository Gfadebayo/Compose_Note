package com.exzell.composenote.preview.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.exzell.composenote.ui.screen.CreateNote
import com.exzell.composenote.ui.theme.ComposeNoteTheme

@Preview(heightDp = 732, widthDp = 412, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CreateNotePreview(){
    ComposeNoteTheme {
        CreateNote()
    }
}