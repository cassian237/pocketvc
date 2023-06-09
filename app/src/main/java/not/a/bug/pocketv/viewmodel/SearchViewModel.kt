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
import not.a.bug.pocketv.SessionManager
import not.a.bug.pocketv.model.AccessTokenNotFoundException
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.repository.PocketRepository
import org.jsoup.Jsoup
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pocketRepository: PocketRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _articles = MutableStateFlow<List<PocketArticle>>(emptyList())
    val articles: StateFlow<List<PocketArticle>> = _articles

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchArticles(searchQuery: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = pocketRepository.getItems(search = searchQuery)) {
                is NetworkResult.Error -> {
                    if (result.exception is AccessTokenNotFoundException) {
                        sessionManager.logOut()
                    }
                }
                is NetworkResult.Loading -> {
                    // handle loading
                }
                is NetworkResult.Success -> {
                    _articles.value = result.data.list.map {
                        val meta = fetchMeta(it.value.resolvedUrl)
                        it.value.copy(
                            resolvedImage = meta.first ?: it.value.images?.asIterable()?.firstOrNull()?.value?.src,
                            resolvedUrl = meta.second ?: URI(it.value.resolvedUrl).host
                        )
                    }
                }
            }

            _isLoading.value = false
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