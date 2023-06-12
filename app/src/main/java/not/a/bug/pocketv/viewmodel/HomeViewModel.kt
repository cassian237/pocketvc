package not.a.bug.pocketv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.repository.PocketRepository
import org.jsoup.Jsoup
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pocketRepository: PocketRepository
) : ViewModel() {

    private val _latestArticles = MutableStateFlow<List<PocketArticle>>(emptyList())
    val latestArticles: StateFlow<List<PocketArticle>> = _latestArticles

    private val _archivedArticles = MutableStateFlow<List<PocketArticle>>(emptyList())
    val archivedArticles: StateFlow<List<PocketArticle>> = _archivedArticles

    private val _favoriteArticles = MutableStateFlow<List<PocketArticle>>(emptyList())
    val favoriteArticles: StateFlow<List<PocketArticle>> = _favoriteArticles

    private val _articleContentType = MutableStateFlow<List<PocketArticle>>(emptyList())
    val articleContentType: StateFlow<List<PocketArticle>> = _articleContentType

    private val _videoContentType = MutableStateFlow<List<PocketArticle>>(emptyList())
    val videoContentType: StateFlow<List<PocketArticle>> = _videoContentType

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadArticles()
    }

    private fun loadArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            val latestArticlesJob = async { loadCategoryArticles(_latestArticles, state = "unread") }
            val archivedArticlesJob = async { loadCategoryArticles(_archivedArticles, state = "archive") }
            val favoriteArticlesJob = async { loadCategoryArticles(_favoriteArticles, favorite = 1) }
            val articleContentTypeJob = async { loadCategoryArticles(_articleContentType, contentType = "article") }
            val videoContentTypeJob = async { loadCategoryArticles(_videoContentType, contentType = "video") }

            latestArticlesJob.await()
            archivedArticlesJob.await()
            favoriteArticlesJob.await()
            articleContentTypeJob.await()
            videoContentTypeJob.await()

            _isLoading.value = false
        }
    }

    private suspend fun loadCategoryArticles(flow: MutableStateFlow<List<PocketArticle>>, state: String? = null, favorite: Int? = null, contentType: String? = null) {
        when (val result = pocketRepository.getItems(state = state, favorite = favorite, contentType = contentType, count = 9, detailType = "complete")) {
            is NetworkResult.Error -> {
                // handle error
            }
            is NetworkResult.Loading -> {
                // handle loading
            }
            is NetworkResult.Success -> {
                flow.value = result.data.list.map {
                    val meta = fetchMeta(it.value.resolvedUrl)
                    it.value.copy(
                        resolvedImage = meta.first ?: it.value.images?.asIterable()?.firstOrNull()?.value?.src,
                        resolvedUrl = meta.second ?: URI(it.value.resolvedUrl).host
                    )
                }
            }
        }
    }

    private suspend fun fetchMeta(url: String): Pair<String?, String?> =
        withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url).get()
                val ogImage = doc.select("meta[property=og:image]").first()
                val siteName = doc.select("meta[property=og:site_name]").first()
                return@withContext Pair(ogImage.attr("content"), siteName.attr("content"))
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
                return@withContext Pair(null, null)
            }
        }
}