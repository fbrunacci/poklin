package poklin.compose.composable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Avatar(
    imagePath: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(imagePath?.let { imagePath } ?: "images/default-avatar.png"),
        contentDescription = "Default Avatar",
        modifier = modifier.aspectRatio(1f)
    )
}

@Preview
@Composable
fun MockAvatar() {
    Row {
        Avatar(
            imagePath = null,
            modifier = Modifier.width(300.dp)
        )
    }
}