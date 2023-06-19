package com.exzell.composenote

import android.os.Bundle
import androidx.activity.compose.setContent
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.exzell.composenote.ui.navigation.RootNavNode
import com.exzell.composenote.ui.theme.ComposeNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : NodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNoteTheme {
                NodeHost(integrationPoint = appyxIntegrationPoint) {
                    RootNavNode(it)
                }
            }
        }
    }
}