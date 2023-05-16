package com.exzell.composenote.preview.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.exzell.composenote.R
import com.exzell.composenote.ui.screen.CreateNote
import com.exzell.composenote.ui.widget.SearchToolbar
import com.exzell.composenote.ui.widget.Toolbar

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun ToolbarPreview() {
    CreateNote()
}

@Preview(widthDp = 412, backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun SearchToolbarPreview() {
    SearchToolbar(
            menuIcons = listOf(R.drawable.ic_grid)
    ) {

    }
}