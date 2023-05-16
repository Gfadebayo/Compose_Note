package com.exzell.composenote.ui.navigation.destination

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.exzell.composenote.ui.navigation.NavDestination
import com.exzell.composenote.ui.screen.Home
import kotlinx.parcelize.Parcelize
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.exzell.composenote.domain.Note

@Parcelize
object HomeDestination: NavDestination {

    override fun createNode(buildContext: BuildContext, backstack: BackStack<NavDestination>): Node {
        return node(buildContext) {
            val create: () -> Unit = { backstack.push(CreateNoteDestination()) }

            val click: (Note) -> Unit = { backstack.push(CreateNoteDestination(it)) }

            Home(onCreateClick = create, onNoteClick = click)
        }
    }
}