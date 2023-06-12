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
import not.a.bug.pocketv.model.ParsedArticle
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.repository.MercuryParserRepository
import not.a.bug.pocketv.repository.PocketRepository
import org.jsoup.Jsoup
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val mercuryParserRepository: MercuryParserRepository
) : ViewModel() {

    private val _article = MutableStateFlow<ParsedArticle?>(null)
    val article: StateFlow<ParsedArticle?> = _article

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getArticleDetail(url: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = mercuryParserRepository.getParsedArticle(url = url)) {
                is NetworkResult.Error -> {
                    // handle error
                }
                is NetworkResult.Loading -> {
                    // handle loading
                }
                is NetworkResult.Success -> {
                    _article.value = result.data
                }
            }

            _isLoading.value = false
        }
    }
}