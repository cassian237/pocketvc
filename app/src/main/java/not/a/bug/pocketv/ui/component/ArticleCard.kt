package not.a.bug.pocketv.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import not.a.bug.pocketv.model.PocketArticle
import java.net.URI

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    article: PocketArticle,
    onClick: () -> Unit
) {
    var isImageResolved by remember { mutableStateOf<Boolean>(article.resolvedImage != null) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(180.dp),
        colors = CardDefaults.colors(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = CardDefaults.border(focusedBorder =
            Border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.border
                )
            )
        )
    ) {
        Column(Modifier.blur(radius = 8.dp)) {
            if (isImageResolved) {
                AsyncImage(
                    model = article.resolvedImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Error) {
                            isImageResolved = false
                        }
                    }
                )
            } else {
                val color = remember {
                    val firstLetter = article.resolvedTitle.firstOrNull()?.uppercaseChar() ?: 'A'
                    val baseColorValue = firstLetter.code
                    Color(
                        red = (baseColorValue * 5 % 128 + 127),
                        green = (baseColorValue * 7 % 128 + 127),
                        blue = (baseColorValue * 11 % 128 + 127)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = article.resolvedTitle.firstOrNull()?.uppercaseChar().toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White
                    )
                }
            }

            Text(
                text = article.resolvedTitle,
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                lineHeight = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .height((3 * 16 + 8).dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = article.resolvedUrl,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}