package not.a.bug.pocketv.ui.component

import android.util.Base64
import android.webkit.WebView
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import not.a.bug.pocketv.viewmodel.ArticleViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ArticleScreen(navController: NavController, articleUrl: String?) {

    val articleViewModel = hiltViewModel<ArticleViewModel>()
    val article by articleViewModel.article.collectAsState()
    val isLoading by articleViewModel.isLoading.collectAsState()
    val colors = MaterialTheme.colorScheme
    val backgroundColor = colors.background.toHex()
    val onBackgroundColor = colors.onBackground.toHex()
    val listState = rememberTvLazyListState()
    val firstFocusRequester = remember { FocusRequester() }

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    article?.let { parsedArticle ->
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier.focusRequester(firstFocusRequester),
                    onClick = { /* TODO: Implement WebView navigation */ },
                ) {
                    Text(text = "Open in WebView")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* TODO: Listen */ },
                ) {
                    Text(text = "Listen")
                }
            }

            TvLazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusable()
            ) {
                item {

                    Text(
                        text = parsedArticle.title,
                        style = MaterialTheme.typography.headlineMedium.copy(color = colors.onBackground),
                        modifier = Modifier.padding(top = 16.dp),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (parsedArticle.leadImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = parsedArticle.leadImageUrl,
                            contentDescription = "Lead Image",
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(4.dp))
                        )
                    }

                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                val base64: String =
                                    Base64.encodeToString(
                                        """
                                    <html>
                                    <head>
                                        <style>
                                        body {
                                            background-color: $backgroundColor;
                                            color: $onBackgroundColor;
                                        }
                                        </style>
                                    </head>
                                    <body>
                                    ${parsedArticle.content}
                                    </body>
                                    </html>
                                    """.trimIndent().toByteArray(),
                                        Base64.DEFAULT
                                    )
                                loadData(base64, "text/html; charset=utf-8", "base64")
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        articleUrl?.let { articleViewModel.getArticleDetail(it) }
    }
    LaunchedEffect(article) {
        article?.let { firstFocusRequester.requestFocus() }
    }
}

fun Color.toHex(): String {
    return String.format(
        "#%02x%02x%02x",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}