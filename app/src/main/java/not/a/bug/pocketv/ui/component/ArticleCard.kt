package not.a.bug.pocketv.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import not.a.bug.pocketv.model.PocketArticle

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ArticleCard(article: PocketArticle,
                onClick: () -> Unit) {
    Card(onClick = onClick) {
        Column {
            article.images.asIterable().firstOrNull()?.let { url ->
                // Load image from the url
                val image = rememberAsyncImagePainter(model = url)
                Image(
                    painter = image,
                    contentDescription = "Article Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                )
            }

            Text(
                text = article.resolvedTitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = article.resolvedUrl,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}