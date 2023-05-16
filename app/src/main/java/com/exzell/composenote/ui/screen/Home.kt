package com.exzell.composenote.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exzell.composenote.R
import com.exzell.composenote.core.di.Constants
import com.exzell.composenote.domain.Note
import com.exzell.composenote.ui.theme.DIMEN_PADDING_HORIZONTAL
import com.exzell.composenote.ui.viewmodel.HomeViewModel
import com.exzell.composenote.ui.widget.SearchToolbar
import com.exzell.composenote.ui.widget.SelectionToolbar
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun createDummyNotes(): List<Note> {
    return buildList {
        add(Note(0, "Merlin", body = "In a land of myth and a time of magic, the destiny of a great kingdom rests on the shoulders of a young man. His name, Merlin", colorFirst = 0xFFFF0000, colorSecond = 0xFFCCCCCC))

        add(Note(1, "Fate", "I am the bone of my sword, steel is my body and fire is my blood, I have created over a thousand blades unknown to death nor known to life. Unlimited Blade Works", colorFirst = 0xFFFF00FF, colorSecond = 0xFFFFFF00))

        add(Note(2, "", colorFirst = 0xFF0000FF, colorSecond = 0xFF00FFFF))
    }
}

@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel(),
         onCreateClick: () -> Unit,
         onNoteClick: (Note) -> Unit) {

    val scope = rememberCoroutineScope()

    val groupSelectionFlow = remember { MutableSharedFlow<Boolean?>(replay = 1) }

    val notes by viewModel.getAllNotes().collectAsState(initial = emptyList())

    val displayMode by viewModel.getDisplayMode().collectAsState(initial = -1)

    var selectionCount by remember { mutableStateOf(0) }

    val onSelectionChanged: (Int) -> Unit = { selectionCount = it }

    val onMenuClick: (Int) -> Unit = { viewModel.switchDisplayMode(displayMode) }

    val onCancelSelection: () -> Unit = { scope.launch { groupSelectionFlow.emit(false) } }

    Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (selectionCount == 0) {
                    val menuIcon = if (displayMode == Constants.DISPLAY_MODE_GRID) R.drawable.ic_single_view
                    else R.drawable.ic_grid

                    SearchToolbar(
                            menuIcons = listOf(menuIcon),
                            onMenuIconClicked = onMenuClick
                    ) {

                    }
                }
                else {
                    SelectionToolbar(
                            onCountChanged = selectionCount.toString(),
                            onCancelClick = onCancelSelection,
                    )
                }
            },
            /*bottomBar = {
                BottomAppBar(elevation = 5.dp) {

                }
            },*/

            floatingActionButton = {
                FloatingActionButton(onClick = onCreateClick,
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary) {
                    Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Add new note")
                }
            },

            floatingActionButtonPosition = FabPosition.End)
    { padding ->
        val realPadding = PaddingValues(start = DIMEN_PADDING_HORIZONTAL,
                end = DIMEN_PADDING_HORIZONTAL,
                bottom = padding.calculateBottomPadding())

        if (displayMode == Constants.DISPLAY_MODE_GRID) {
            HomeGridContent(notes = notes,
                    selectionStateFlow = groupSelectionFlow,
                    paddingValues = realPadding,
                    onItemClick = onNoteClick,
                    onSelectionChanged = onSelectionChanged)
        }
        else if (displayMode == Constants.DISPLAY_MODE_LIST) {
            HomeListContent(notes = notes,
                    selectionStateFlow = groupSelectionFlow,
                    paddingValues = realPadding,
                    onItemClick = onNoteClick,
                    onSelectionChanged = onSelectionChanged)
        }
    }
}

/**
 * @see HomeGridContent
 */
@Composable
fun HomeListContent(
        modifier: Modifier = Modifier,
        notes: List<Note>,
        selectionStateFlow: Flow<Boolean?> = emptyFlow(),
        paddingValues: PaddingValues,
        onLastItemAdded: (() -> Unit)? = null,
        onSelectionChanged: ((Int) -> Unit)? = null,
        onItemClick: (Note) -> Unit
) {
    val selectedItems = remember { mutableStateListOf<Long>() }

    LaunchedEffect(Unit) {
        selectionStateFlow.collect { isAllSelected ->
            when (isAllSelected) {
                true -> {
                    selectedItems.addAll(notes.map { it.id }.filterNot { selectedItems.contains(it) })
                    onSelectionChanged?.invoke(selectedItems.size)
                }

                false -> {
                    selectedItems.clear()
                    onSelectionChanged?.invoke(selectedItems.size)
                }

                else -> {}
            }
        }
    }

    LazyColumn(modifier = modifier,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(notes) { index, note ->
            val onItemSelected: (Int) -> Unit = {
                if (it == CLICK_SINGLE && selectedItems.isEmpty()) {
                    //Go to the next screen
                    onItemClick(note)
                }
                else if (it == CLICK_SINGLE && !selectedItems.contains(note.id)) {
                    //Add the item to the selected list
                    selectedItems.add(note.id)
                    onSelectionChanged?.invoke(selectedItems.size)
                }
                else if (it == CLICK_SINGLE && selectedItems.contains(note.id)) {
                    //Remove the item from the selected list
                    selectedItems.remove(note.id)
                    onSelectionChanged?.invoke(selectedItems.size)
                }
                else if (it == CLICK_LONG && !selectedItems.contains(note.id)) {
                    //Long click's only job is to add the item to the list
                    selectedItems.add(note.id)
                    onSelectionChanged?.invoke(selectedItems.size)
                }
            }

            HomeItem(note = note,
                    isSelected = selectedItems.contains(note.id),
                    onClicked = onItemSelected)

            if (index == notes.size - 1 && onLastItemAdded != null) onLastItemAdded()
        }
    }
}

private const val CLICK_SINGLE = 0
private const val CLICK_LONG = 1

/**
 * @param notes the list of notes to display
 * @param paddingValues padding constraint the Composable should adapt to
 * @param onLastItemAdded called when the last item has been rendered
 * @param onSelectionChanged called when the selection state of a note has changed.
 *                           It sends the current number of selected notes
 * @param onItemClick called when a note has been single clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeGridContent(
        modifier: Modifier = Modifier,
        notes: List<Note>,
        selectionStateFlow: Flow<Boolean?> = emptyFlow(),
        paddingValues: PaddingValues,
        onLastItemAdded: (() -> Unit)? = null,
        onSelectionChanged: ((Int) -> Unit)? = null,
        onItemClick: (Note) -> Unit) {

    //The ids of the currently selected items
    val selectedItems = remember { mutableStateListOf<Long>() }

    LaunchedEffect(key1 = Unit) {
        selectionStateFlow.collect { isAllSelected ->
            when (isAllSelected) {
                true -> {
                    selectedItems.addAll(notes.map { it.id }.filterNot { selectedItems.contains(it) })
                    onSelectionChanged?.invoke(selectedItems.size)
                }

                false -> {
                    selectedItems.clear()
                    onSelectionChanged?.invoke(selectedItems.size)
                }

                else -> {}
            }
        }
    }

    LazyVerticalStaggeredGrid(modifier = modifier, columns = StaggeredGridCells.Fixed(2),
            contentPadding = paddingValues,
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(notes, key = { index, _ -> index }) { index, note ->

            val onItemSelected: (Int) -> Unit = {
                if (it == CLICK_SINGLE && selectedItems.isEmpty()) {
                    //Go to the next screen
                    onItemClick(note)
                }
                else if (it == CLICK_SINGLE && !selectedItems.contains(note.id)) {
                    //Add the item to the selected list
                    selectedItems.add(note.id)
                    onSelectionChanged?.invoke(selectedItems.size)
                }
                else if (it == CLICK_SINGLE && selectedItems.contains(note.id)) {
                    //Remove the item from the selected list
                    selectedItems.remove(note.id)
                    onSelectionChanged?.invoke(selectedItems.size)
                }
                else if (it == CLICK_LONG && !selectedItems.contains(note.id)) {
                    //Long click's only job is to add the item to the list
                    selectedItems.add(note.id)
                    onSelectionChanged?.invoke(selectedItems.size)
                }
                else {
                }
            }

            HomeItem(
                    modifier = Modifier,
                    note = note,
                    isSelected = selectedItems.contains(note.id),
                    onClicked = onItemSelected)

            if (index == notes.size - 1 && onLastItemAdded != null) onLastItemAdded()
        }
    }
}

/**
 * @param isSelected the state of the item
 * @param onClicked called each time the item is clicked (single or long)
 * this helps to handle whether the item should display its selected state or not
 * The values passed in are either [CLICK_SINGLE] or [CLICK_LONG]
 * return true to display the selected state and false to not display it
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeItem(
        modifier: Modifier = Modifier,
        note: Note,
        isSelected: Boolean = false,
        onClicked: (Int) -> Unit
) {
    val borderWidth = if(isSelected) 4.dp else 1.dp
    val backgroundColor = if(!isSelected) Color(note.colorFirst).compositeOver(Color.White)
    else Color(note.colorFirst)

    val borderColor = if(!isSelected) Color(note.colorSecond) else Color.Blue

    val newModifier = modifier
            .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .combinedClickable(
                    onLongClick = { onClicked(CLICK_LONG) },
                    onClick = { onClicked(CLICK_SINGLE) }
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)

    Column(modifier = newModifier) {

        Text(text = note.title, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = note.body,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 12,
                overflow = TextOverflow.Ellipsis)
    }
}

@Preview(group = "List", device = Devices.NEXUS_7)
@Composable
fun HomeListPreview() {
    var lastItemAdded by remember { mutableStateOf(false) }

    var selectedSize by remember { mutableStateOf(0) }

    var isGrid by remember { mutableStateOf(true) }

    val onLastItemAdded: () -> Unit = { lastItemAdded = true }

    val onSelectionChanged: (Int) -> Unit = { selectedSize = it }

    val stateFlow = MutableStateFlow<Boolean?>(null)

    Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)) {
        Text(modifier = Modifier.clickable { isGrid = !isGrid },
                text = "Last item added: $lastItemAdded and isGrid to be $isGrid")

        Text(modifier = Modifier.clickable {
            stateFlow.value = !(stateFlow.value ?: false)
        }, text = "Currently selected item size: $selectedSize")

        val weightModifier = Modifier.weight(1f)

        if (!isGrid) {
            HomeListContent(
                    modifier = weightModifier,
                    notes = createDummyNotes(),
                    paddingValues = PaddingValues(horizontal = 6.dp, vertical = 36.dp),
                    onLastItemAdded = onLastItemAdded,
                    onSelectionChanged = onSelectionChanged,
                    onItemClick = {})
        }
        else {
            HomeGridContent(
                    modifier = weightModifier,
                    notes = createDummyNotes(),
                    paddingValues = PaddingValues(horizontal = 6.dp, vertical = 36.dp),
                    onLastItemAdded = onLastItemAdded,
                    onSelectionChanged = onSelectionChanged,
                    onItemClick = {})
        }
    }
}