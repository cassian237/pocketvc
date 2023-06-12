package not.a.bug.pocketv.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import not.a.bug.pocketv.model.PocketArticle

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ListForArticles(title: String, articles: List<PocketArticle>, onArticleClicked : (PocketArticle) -> Unit) {
    if (articles.isNotEmpty()) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    TvLazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(articles) { article ->
            ArticleCard(article = article) { onArticleClicked(article) }
        }
    }
}