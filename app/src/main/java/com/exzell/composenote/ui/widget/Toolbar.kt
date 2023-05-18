package com.exzell.composenote.ui.widget

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.exzell.composenote.R

private val DEFAULT_SIZE = 56.dp

@Composable
fun Toolbar(
        modifier: Modifier = Modifier,
        showUp: Boolean = true,
        onUpClicked: (() -> Unit)? = null,
        menuIcons: List<Int> = emptyList(),
        onMenuIconClicked: ((Int) -> Unit)? = null
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Row(modifier = modifier.height(DEFAULT_SIZE)) {
        if (showUp) {
            IconButton(onClick = { onUpClicked?.invoke() ?: backDispatcher?.onBackPressed() }) {
                Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Navigate up")
            }
        }

        Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .background(Color.Red))

        menuIcons.forEachIndexed { index, imageVector ->
            IconButton(onClick = { onMenuIconClicked?.invoke(index) }) {
                Icon(painter = painterResource(id = imageVector), contentDescription = "An image vector")
            }
        }
    }
}

@Composable
fun SearchToolbar(
        menuIcons: List<Int> = emptyList(),
        onMenuIconClicked: ((Int) -> Unit)? = null,
        onNavIconClicked: () -> Unit
) {
    Row(modifier = Modifier.height(DEFAULT_SIZE).zIndex(200f),
            verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onNavIconClicked() }) {
            Icon(painter = painterResource(id = R.drawable.ic_nav_drawer), contentDescription = "Open navigation drawer")
        }

        Text(text = stringResource(id = R.string.search_your_notes),
            modifier = Modifier.weight(1f))

        menuIcons.forEachIndexed { index, iconRes ->
            IconButton(onClick = { onMenuIconClicked?.invoke(index) }) {
                Icon(painter = painterResource(id = iconRes), contentDescription = "Icon")
            }
        }
    }
}

@Composable
fun SelectionToolbar(
        modifier: Modifier = Modifier,
        onCountChanged: String,
        textStyle: TextStyle = LocalTextStyle.current,
        menuIcons: List<Int> = emptyList(),
        onMenuClick: ((Int) -> Unit)? = null,
        onCancelClick: () -> Unit,

) {
    Row(modifier = modifier.height(DEFAULT_SIZE),
            verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onCancelClick() }) {
            Icon(painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Clear selection")
        }

        Text(modifier = Modifier.weight(1f), text = onCountChanged, style = textStyle)

        menuIcons.forEachIndexed { index, iconRes ->
            IconButton(onClick = { onMenuClick?.invoke(iconRes) }) {
                Icon(painter = painterResource(id = iconRes), contentDescription = "Icon")
            }
        }
    }
}