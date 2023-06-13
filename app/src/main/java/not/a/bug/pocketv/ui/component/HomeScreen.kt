package not.a.bug.pocketv.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.ExperimentalTvMaterial3Api
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.viewmodel.HomeViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel, onArticleClicked : (PocketArticle) -> Unit) {
    val latestArticles by homeViewModel.latestArticles.collectAsState()
    val archivedArticles by homeViewModel.archivedArticles.collectAsState()
    val favoriteArticles by homeViewModel.favoriteArticles.collectAsState()
    val articleContentType by homeViewModel.articleContentType.collectAsState()
    val videoContentType by homeViewModel.videoContentType.collectAsState()

    val isLoading by homeViewModel.isLoading.collectAsState()

    val focusRequester = remember { FocusRequester() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                TvLazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    content = {
                        item {
                            ImmersiveListForArticles(Modifier.focusRequester(focusRequester),"Latest Articles", latestArticles, onArticleClicked)
                        }
                        item {
                            ListForArticles("Archived Articles", archivedArticles, onArticleClicked)
                        }
                        item {
                            ListForArticles("Favorite Articles", favoriteArticles, onArticleClicked)
                        }
                        item {
                            ListForArticles("Articles", articleContentType, onArticleClicked)
                        }
                        item {
                            ListForArticles("Videos", videoContentType, onArticleClicked)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    })
            }
        }
    }

    LaunchedEffect(Unit) {
        if(!isLoading) {
            focusRequester.requestFocus()
        }
    }
}