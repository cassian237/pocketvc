package not.a.bug.pocketv.ui.component

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveList
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import kotlinx.coroutines.launch
import not.a.bug.pocketv.model.PocketArticle

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveListForArticles(
    modifier: Modifier = Modifier,
    title: String,
    articles: List<PocketArticle>,
    onArticleClicked: (PocketArticle) -> Unit
) {
    if (articles.isNotEmpty()) {
        val imageHeight = 292.dp
        var listHasFocus by remember { mutableStateOf(false) }

        ImmersiveList(
            modifier = modifier,
            background = { index, hasFocus ->
                val article = articles[index]
                listHasFocus = hasFocus
                val imageSize = with(LocalDensity.current) {
                    IntSize(
                        ((imageHeight.toPx() / 9f) * 16f).toInt(),
                        imageHeight.toPx().toInt()
                    )
                }
                var isImageResolved by remember { mutableStateOf<Boolean>(articles[index].resolvedImage != null) }

                val animatedHeight by animateDpAsState(
                    targetValue = if (hasFocus) imageHeight else 54.dp,
                    animationSpec = tween() // you can adjust the duration here
                )

                val columnEnterTransition =
                    updateTransition(targetState = hasFocus, label = "column enter transition")
                val columnOffsetY by columnEnterTransition.animateDp(
                    transitionSpec = {
                        if (hasFocus) {
                            tween(500)
                        } else {
                            snap()
                        }
                    },
                    label = "column offset Y"
                ) { focused -> if (focused) 0.dp else 300.dp }

                val alpha by columnEnterTransition.animateFloat(
                    transitionSpec = {
                        if (hasFocus) {
                            tween(500)
                        } else {
                            snap()
                        }
                    },
                    label = "alpha"
                ) { focused -> if (focused) 1f else 0f }

                Box(
                    Modifier
                        .height(if (hasFocus) animatedHeight else 54.dp)
                        .fillMaxWidth()
                ) {
                    if (hasFocus) {
                        Box(
                            Modifier
                                .height(imageHeight)
                                .fillMaxWidth()
                        ) {
                            isImageResolved = articles[index].resolvedImage != null
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .background(MaterialTheme.colorScheme.background),
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            if (animatedHeight == imageHeight) {
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
                            }

                            // Horizontal gradient overlay
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



                            Column(
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp)
                                    .align(Alignment.CenterStart)
                                    .width(550.dp)
                                    .offset(y = columnOffsetY)
                                    .alpha(alpha)
                            ) {
                                Text(
                                    text = articles[index].resolvedTitle,
                                    style = MaterialTheme.typography.headlineLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(28.dp))
                                Text(
                                    text = articles[index].excerpt,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(48.dp))
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(16.dp),
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }) {
            val focusRequester = remember { FocusRequester() }
            val lazyRowHasFocus = remember { mutableStateOf(listHasFocus) }

            TvLazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = if (listHasFocus) 212.dp else 48.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(articles) { index, article ->
                    var modifier = Modifier.immersiveListItem(index)
                    if (index == 0) modifier = modifier.focusRequester(focusRequester)
                    ArticleCard(modifier = modifier, article) { onArticleClicked(article) }

                    LaunchedEffect(listHasFocus) {
                        if (listHasFocus && !lazyRowHasFocus.value) {
                            try {
                                focusRequester.requestFocus()
                            } catch (_: Exception) {
                            }
                        }
                        lazyRowHasFocus.value = listHasFocus
                    }
                }
            }
        }
    }
}