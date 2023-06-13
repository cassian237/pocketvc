package not.a.bug.pocketv.ui.component

import android.net.Uri
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Base64
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch
import not.a.bug.pocketv.viewmodel.ArticleViewModel
import java.util.Locale

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
    val coroutineScope = rememberCoroutineScope()

    var displayWebView by remember { mutableStateOf(false) }
    val webViewContent = remember { mutableStateOf("") }

    val listItemHeight = remember { mutableStateOf(0) }

    var sentences by remember { mutableStateOf(listOf<String>()) }
    var currentSentenceIndex by remember { mutableStateOf(0) }
    var isSpeaking by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context, null)
    }

    LaunchedEffect(tts) {
        tts.language = Locale.getDefault()
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                isSpeaking = true
            }

            override fun onDone(utteranceId: String?) {
                if (currentSentenceIndex < sentences.size - 1) {
                    currentSentenceIndex++
                    tts.speak(sentences[currentSentenceIndex], TextToSpeech.QUEUE_FLUSH, null, "sentence")
                } else {
                    currentSentenceIndex = 0
                    isSpeaking = false
                }
            }

            override fun onError(utteranceId: String?) {
                isSpeaking = false
                currentSentenceIndex = 0
            }
        })
    }

    LaunchedEffect(currentSentenceIndex) {
        val size = sentences.size
        if(currentSentenceIndex == 1) {
            listState.animateScrollBy(200f)
        }
        if (size > 0) {
            val progressPercentage = currentSentenceIndex.toFloat() / size.toFloat()
            val scrollByPixels = progressPercentage * listItemHeight.value
            listState.animateScrollBy(scrollByPixels)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    BackHandler(listState.canScrollBackward) {
        coroutineScope.launch {
            listState.animateScrollToItem(0)
            firstFocusRequester.requestFocus()
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
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
                    onClick = {
                        displayWebView = !displayWebView
                    }, // Toggle the display mode when the button is clicked
                ) {
                    Text(text = if (displayWebView) "Open as Text" else "Open in WebView") // Change the button text based on the display mode
                }
                if (!displayWebView) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (isSpeaking) {
                                tts.stop()
                                currentSentenceIndex = 0
                            } else {
                                if (sentences.isNotEmpty()) {
                                    tts.speak(sentences[currentSentenceIndex], TextToSpeech.QUEUE_FLUSH, null, "sentence")
                                }
                            }
                            isSpeaking = !isSpeaking
                        },
                    ) {
                        Text(text = if (isSpeaking) "Stop Listening" else "Listen")
                    }
                }
            }

            TvLazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onSizeChanged { size ->
                        listItemHeight.value = size.height
                    }
                    .onKeyEvent { keyEvent ->
                        when (keyEvent.key) {
                            Key.DirectionUp -> {
                                if (listState.canScrollBackward) {
                                    coroutineScope.launch {
                                        listState.animateScrollBy(-150f)
                                    }
                                    true
                                } else false
                            }

                            Key.DirectionDown -> {
                                coroutineScope.launch {
                                    listState.animateScrollBy(100f)
                                }
                                true
                            }

                            else -> false
                        }
                    }
                    .focusable()
            ) {
                item {
                    if (displayWebView) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(
                                            view: WebView?,
                                            request: WebResourceRequest?
                                        ): Boolean {
                                            request?.let {
                                                // navigate to new screen with url as parameter
                                                navController.popBackStack()
                                                navController.navigate(
                                                    "articleScreen/${
                                                        Uri.encode(
                                                            it.url.toString()
                                                        )
                                                    }"
                                                )
                                                return true
                                            }
                                            return false
                                        }
                                    }
                                    settings.javaScriptEnabled = true
                                    loadUrl(articleUrl ?: "")
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    } else {
                        parsedArticle.title?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineMedium.copy(color = colors.onBackground),
                                modifier = Modifier.padding(top = 16.dp),
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!parsedArticle.leadImageUrl.isNullOrEmpty()) {
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
                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)

                                            // When the page finishes loading, retrieve the content and store it in webViewContent
                                            evaluateJavascript(
                                                "(function() { return document.body.innerText; })();"
                                            ) { result ->
                                                parsedArticle.title?.let {
                                                    webViewContent.value = it
                                                }
                                                webViewContent.value += result
                                                sentences = webViewContent.value.split(".")
                                            }
                                        }

                                        override fun shouldOverrideUrlLoading(
                                            view: WebView?,
                                            request: WebResourceRequest?
                                        ): Boolean {
                                            request?.let {
                                                // navigate to new screen with url as parameter
                                                navController.popBackStack()
                                                navController.navigate(
                                                    "articleScreen/${
                                                        Uri.encode(
                                                            it.url.toString()
                                                        )
                                                    }"
                                                )
                                                return true
                                            }
                                            return false
                                        }
                                    }
                                    settings.javaScriptEnabled = true
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