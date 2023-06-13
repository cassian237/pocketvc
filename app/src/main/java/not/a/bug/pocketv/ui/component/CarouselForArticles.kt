package not.a.bug.pocketv.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Button
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.ui.theme.JetStreamCardShape

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CarouselForArticle(articles: List<PocketArticle>, onArticleClicked : (PocketArticle) -> Unit) {
    val carouselState = remember { CarouselState() }

    Carousel(
        itemCount = 4,
        modifier = Modifier
            .height(300.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        carouselState = carouselState,
        carouselIndicator = {
            CarouselDefaults.IndicatorRow(
                itemCount = 4,
                activeItemIndex = carouselState.activeItemIndex,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            )
        }
    ) { itemIndex ->
        val article = articles[itemIndex]
        var isImageResolved = articles[itemIndex].resolvedImage != null
        val imageHeight = 300.dp
        val imageSize = with(LocalDensity.current) {
            IntSize(
                ((imageHeight.toPx() / 9f) * 16f).toInt(),
                imageHeight.toPx().toInt()
            )
        }
        CarouselItem(modifier = Modifier
            .border(2.dp, MaterialTheme.colorScheme.border.copy(alpha = 0.5f), JetStreamCardShape),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (isImageResolved) {
                        AsyncImage(
                            model = article.resolvedImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(imageHeight)
                                .align(Alignment.TopEnd)
                                .aspectRatio(16f / 9f),
                            onState = { state ->
                                if (state is AsyncImagePainter.State.Error) {
                                    isImageResolved = false
                                }
                            }
                        )
                    } else {
                        val color = remember {
                            val firstLetter =
                                article.resolvedTitle.firstOrNull()?.uppercaseChar()
                                    ?: 'A'
                            val baseColorValue = firstLetter.code
                            Color(
                                red = (baseColorValue * 5 % 128 + 127),
                                green = (baseColorValue * 7 % 128 + 127),
                                blue = (baseColorValue * 11 % 128 + 127)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(imageHeight)
                                .aspectRatio(16f / 9f)
                                .align(Alignment.TopEnd)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = article.resolvedTitle.firstOrNull()
                                    ?.uppercaseChar()
                                    .toString(),
                                style = MaterialTheme.typography.displayLarge,
                                color = Color.White
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(imageHeight)
                            .aspectRatio(16f / 9f)
                            .align(Alignment.TopEnd)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        Color.Transparent
                                    ),
                                    startX = 0f, // Gradient starts from the left
                                    endX = imageSize.width / 1.5f // and ends at the right
                                )
                            )
                    )
                    // Horizontal gradient overlay
                    Box(
                        modifier = Modifier
                            .height(imageHeight)
                            .aspectRatio(16f / 9f)
                            .align(Alignment.TopEnd)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        Color.Transparent
                                    ),
                                    startY = imageSize.height.toFloat(), // Gradient starts from the left
                                    endY = imageSize.height / 4f// and ends at the right
                                )
                            )
                    )
                }
            }
        ) {
            var isFocused by remember { mutableStateOf(false) }

            Column(
                Modifier
                    .padding(32.dp)
                    .width(550.dp)
            ) {
                Text(
                    text = article.resolvedTitle,
                    style = MaterialTheme.typography.headlineLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = article.excerpt,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onArticleClicked(article) },
                    modifier = Modifier
                        .onFocusChanged { isFocused = it.isFocused }
                        .padding(vertical = 2.dp, horizontal = 5.dp)
                ) {
                    Text(text = "Read")
                }
            }
        }
    }
}