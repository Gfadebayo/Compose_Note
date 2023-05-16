package twitter.theme

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TwitterTheme(content: @Composable () -> Unit) {
    MaterialTheme() {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false, content = content)
    }
}