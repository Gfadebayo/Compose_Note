package com.exzell.composenote.ui.navigation

import android.os.Parcelable
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack

interface NavDestination : Parcelable {
    fun createNode(buildContext: BuildContext, backstack: BackStack<NavDestination>): Node
}
