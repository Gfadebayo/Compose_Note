package com.exzell.composenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.exzell.composenote.domain.Note
import com.exzell.composenote.ui.navigation.RootNavNode
import com.exzell.composenote.ui.screen.Home
import com.exzell.composenote.ui.theme.ComposeNoteTheme
import com.exzell.composenote.ui.viewmodel.HomeViewModel
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