package com.exzell.composenote.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exzell.composenote.R
import com.exzell.composenote.domain.Note
import com.exzell.composenote.ui.theme.ComposeNoteTheme
import com.exzell.composenote.ui.viewmodel.HomeViewModel
import com.exzell.composenote.ui.widget.Toolbar

@Preview(showBackground = true, device = Devices.PIXEL_2_XL)
@Composable
fun CreateNotePreview() {
    ComposeNoteTheme {
        CreateNote()
    }
}

@Composable
fun CreateNote(
        viewModel: HomeViewModel = hiltViewModel(),
        note: Note? = null,
) {
    var newNote by remember { mutableStateOf(note ?: Note()) }

    val menuIcons = listOf(R.drawable.ic_push_pin, R.drawable.ic_alarm_add, R.drawable.ic_move_to_inbox)
    fun onMenuClicked(index: Int) {

    }

    Column(modifier = parentModifier) {
        Toolbar(menuIcons = menuIcons, onMenuIconClicked = { onMenuClicked(it) })

        Spacer(modifier = Modifier.height(8.dp))

        TextField(modifier = maxWidthModifier,
                value = newNote.title,
                onValueChange = { newNote = newNote.copy(title = it) },
                singleLine = true,
                placeholder = { Text("Title") },
                shape = RoundedCornerShape(0.dp),
                colors = textFieldColors,
                keyboardOptions = titleKeyboardOptions)

        Spacer(Modifier.height(4.dp))

        TextField(modifier = maxWidthModifier.weight(1f),
                value = newNote.body,
                onValueChange = { newNote = newNote.copy(body = it) },
                placeholder = { Text("Content") },
                colors = textFieldColors,
                keyboardOptions = contentKeyboardOptions)
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.saveNote(newNote.copy(colorFirst = android.graphics.Color.TRANSPARENT.toLong(),
                    colorSecond = android.graphics.Color.LTGRAY.toLong()))
        }
    }
}

private val textFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
    )

private val parentModifier: Modifier
    @Composable inline get() = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)

private val maxWidthModifier: Modifier
    @Composable inline get() = Modifier.fillMaxWidth()

private val titleKeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = true,
        imeAction = ImeAction.Next)

private val contentKeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = true,
        imeAction = ImeAction.None)
