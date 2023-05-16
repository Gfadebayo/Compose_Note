package com.exzell.composenote.ui.widget

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MenuDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.exzell.composenote.R

@Composable
fun Toolbar(
        modifier: Modifier = Modifier,
        showUp: Boolean = true,
        onUpClicked: (() -> Unit)? = null,
        menuIcons: List<Int> = emptyList(),
        onMenuIconClicked: ((Int) -> Unit)? = null
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Row(modifier = modifier) {
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
    Row(modifier = Modifier.zIndex(200f), verticalAlignment = Alignment.CenterVertically) {
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
        onCountChanged: String,
        menuIcons: List<Int> = emptyList(),
        onMenuClick: ((Int) -> Unit)? = null,
        onCancelClick: () -> Unit,

) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onCancelClick() }) {
            Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = "Clear selection")
        }

        Text(text = onCountChanged)

        menuIcons.forEachIndexed { index, iconRes ->
            IconButton(onClick = { onMenuClick?.invoke(index) }) {
                Icon(painter = painterResource(id = iconRes), contentDescription = "Icon")
            }
        }
    }
}