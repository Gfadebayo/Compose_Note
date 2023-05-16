package com.exzell.composenote.ui.navigation.destination

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.exzell.composenote.domain.Note
import com.exzell.composenote.ui.navigation.NavDestination
import com.exzell.composenote.ui.screen.CreateNote
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateNoteDestination(private val note: Note? = null): NavDestination {

    override fun createNode(buildContext: BuildContext, backstack: BackStack<NavDestination>): Node {
        return node(buildContext) {
            CreateNote(note = note)
        }
    }
}