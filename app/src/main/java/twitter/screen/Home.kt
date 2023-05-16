package twitter.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.exzell.composenote.R

@Composable
fun HomeScreen() {

}

@Composable
fun Toolbar(
        image: String = "",
        onSettingsClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(painter = painterResource(id = R.drawable.ic_person), contentDescription = "")

        TextField(modifier = Modifier.weight(1f), value = "", onValueChange = { }, label = { Text(text = "Search Twitter") }, shape = RoundedCornerShape(percent = 50))

        IconButton(onClick = onSettingsClick) {
            Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToolbarPreview() {
    Toolbar(image = "") {

    }
}