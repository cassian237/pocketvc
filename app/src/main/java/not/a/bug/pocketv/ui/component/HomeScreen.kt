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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.ExperimentalTvMaterial3Api
import not.a.bug.pocketv.viewmodel.HomeViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val latestArticles by homeViewModel.latestArticles.collectAsState()
    val archivedArticles by homeViewModel.archivedArticles.collectAsState()
    val favoriteArticles by homeViewModel.favoriteArticles.collectAsState()
    val articleContentType by homeViewModel.articleContentType.collectAsState()
    val videoContentType by homeViewModel.videoContentType.collectAsState()

    val isLoading by homeViewModel.isLoading.collectAsState()

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
                            ImmersiveListForArticles("Latest Articles", latestArticles)
                        }
                        item {
                            ListForArticles("Archived Articles", archivedArticles)
                        }
                        item {
                            ListForArticles("Favorite Articles", favoriteArticles)
                        }
                        item {
                            ListForArticles("Articles", articleContentType)
                        }
                        item {
                            ListForArticles("Videos", videoContentType)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    })
            }
        }
    }
}