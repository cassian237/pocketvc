package not.a.bug.pocketv.ui.component

import android.view.KeyEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import not.a.bug.pocketv.R
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.ui.theme.JetStreamCardShape
import not.a.bug.pocketv.viewmodel.HomeViewModel
import not.a.bug.pocketv.viewmodel.SearchViewModel

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    homeViewModel: HomeViewModel,
    onArticleClicked : (PocketArticle) -> Unit
) {
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val articles by searchViewModel.articles.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val tfFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val tfInteractionSource = remember { MutableInteractionSource() }
    val isTfFocused by tfInteractionSource.collectIsFocusedAsState()
    val tvLazyColumnState = rememberTvLazyListState()

    fun searchArticles(query: String) {
        searchViewModel.searchArticles(query)
    }

    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = tvLazyColumnState
    ) {
        item {
            Surface(
                shape = ClickableSurfaceDefaults.shape(shape = JetStreamCardShape),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
                color = ClickableSurfaceDefaults.color(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    focusedColor = MaterialTheme.colorScheme.inverseOnSurface,
                    pressedColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                contentColor = ClickableSurfaceDefaults.contentColor(
                    focusedColor = MaterialTheme.colorScheme.onSurface,
                    pressedColor = MaterialTheme.colorScheme.onSurface
                ),
                border = ClickableSurfaceDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = if (isTfFocused) 2.dp else 1.dp,
                            color = animateColorAsState(
                                targetValue = if (isTfFocused) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.border, label = ""
                            ).value
                        ),
                        shape = JetStreamCardShape
                    )
                ),
                tonalElevation = 2.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                onClick = { tfFocusRequester.requestFocus() }
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { updatedQuery -> searchQuery = updatedQuery },
                    decorationBox = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .padding(start = 20.dp),
                        ) {
                            it()
                            if (searchQuery.isEmpty()) {
                                Text(
                                    modifier = Modifier.graphicsLayer { alpha = 0.6f },
                                    text = stringResource(R.string.search_screen_et_placeholder),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        )
                        .focusRequester(tfFocusRequester)
                        .onKeyEvent {
                            if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                                when (it.nativeKeyEvent.keyCode) {
                                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }

                                    KeyEvent.KEYCODE_DPAD_UP -> {
                                        focusManager.moveFocus(FocusDirection.Up)
                                    }

                                    KeyEvent.KEYCODE_BACK -> {
                                        focusManager.moveFocus(FocusDirection.Exit)
                                    }
                                }
                            }
                            true
                        },
                    cursorBrush = Brush.verticalGradient(
                        colors = listOf(
                            LocalContentColor.current,
                            LocalContentColor.current,
                        )
                    ),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchArticles(searchQuery)
                        }
                    ),
                    maxLines = 1,
                    interactionSource = tfInteractionSource,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            if (isLoading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else if (articles.isEmpty() && homeViewModel.latestArticles.value.isNotEmpty()) {
                CarouselForArticle(homeViewModel.latestArticles.value, onArticleClicked)
            } else {
                ListForArticles(
                    title = "Result",
                    articles = articles,
                    onArticleClicked
                )
            }
        }
    }
}