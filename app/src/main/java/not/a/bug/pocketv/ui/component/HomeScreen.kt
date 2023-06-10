package not.a.bug.pocketv.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import not.a.bug.pocketv.viewmodel.HomeViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val articles by homeViewModel.articles.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadArticles()
    }

    TvLazyVerticalGrid(columns = TvGridCells.Adaptive(200.dp), content = {
        items(articles) { article ->
            ArticleCard(article) {}
        }
    })
}