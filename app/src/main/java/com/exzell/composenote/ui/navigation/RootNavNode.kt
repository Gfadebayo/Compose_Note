package com.exzell.composenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.exzell.composenote.ui.navigation.destination.HomeDestination

private val BuildContext.backstackModel: BackStack<NavDestination>
    get() = BackStack(initialElement = HomeDestination, savedStateMap = this.savedStateMap)

class RootNavNode(buildContext: BuildContext,
                  private val backStack: BackStack<NavDestination> = buildContext.backstackModel)
    : ParentNode<NavDestination>(buildContext = buildContext, navModel = backStack) {

    override fun resolve(navTarget: NavDestination, buildContext: BuildContext): Node {
        return navTarget.createNode(buildContext, backStack)
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(navModel = navModel)
    }
}