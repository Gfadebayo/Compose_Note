package com.exzell.composenote.preview.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exzell.composenote.PhonePreview
import com.exzell.composenote.domain.Note
import com.exzell.composenote.ui.screen.HomeGridContent
import com.exzell.composenote.ui.screen.HomeListContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

//@Preview
@Composable
fun HomeItemPreview() {

}

@Preview(group = "Grid", device = Devices.AUTOMOTIVE_1024p)
@Composable
fun HomeGridPreview() {
    val onNoteClick: (Note) -> Unit = {
        println("Note $it clicked")
    }

    HomeGridContent(
            notes = createDummyNotes(),
            paddingValues = PaddingValues(horizontal = 6.dp, vertical = 16.dp),
    onLastItemAdded = { println("The last item has been added")  }, onItemClick = onNoteClick)

}

@Preview(group = "List", device = Devices.NEXUS_7)
@Composable
fun HomeListPreview() {
    var lastItemAdded by remember { mutableStateOf(false) }

    var selectedSize by remember { mutableStateOf(0) }

    var isGrid by remember { mutableStateOf(true) }

    val onLastItemAdded: () -> Unit = { lastItemAdded = true }

    val onSelectionChanged: (Int) -> Unit = { selectedSize = it }

    val flow = flow {
        emit(null)

        delay(1000)

        emit(false)

        delay(1000)

        emit(true)

        delay(5000)

        emit(false)
    }

    Column(modifier = Modifier.background(Color.Yellow)) {
        Text(modifier = Modifier.clickable { isGrid = !isGrid },
                text = "Last item added: $lastItemAdded and isGrid to be $isGrid")

        Text(modifier = Modifier.clickable {

        }, text = "Currently selected item size: $selectedSize")

        if(!isGrid) {
            HomeListContent(notes = createDummyNotes(),
                    paddingValues = PaddingValues(horizontal = 6.dp, vertical = 36.dp),
                    onLastItemAdded = onLastItemAdded,
                    onSelectionChanged = onSelectionChanged,
                    onItemClick = {})
        }
        else {
            HomeGridContent(notes = createDummyNotes(),
                    paddingValues =PaddingValues(horizontal = 6.dp, vertical = 36.dp),
                    onLastItemAdded = onLastItemAdded,
                    onSelectionChanged = onSelectionChanged,
                    onItemClick = {})
        }
    }
}

fun createDummyNotes(): List<Note> {
    return buildList {
        add(Note(0, "Merlin", body = "In a land of myth and a time of magic, the destiny of a great kingdom rests on the shoulders of a young man. His name, Merlin", colorFirst = 0xFFFF0000, colorSecond = 0xFFCCCCCC))

        add(Note(1, "Fate", "I am the bone of my sword, steel is my body and fire is my blood, I have created over a thousand blades unknown to death nor known to life. Unlimited Blade Works", colorFirst = 0xFFFF00FF, colorSecond = 0xFFFFFF00))

        add(Note(2, "", colorFirst = 0xFF0000FF, colorSecond = 0xFF00FFFF))
    }
}