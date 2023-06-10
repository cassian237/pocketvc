package not.a.bug.pocketv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.repository.PocketRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pocketRepository: PocketRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<PocketArticle>>(emptyList())
    val articles: StateFlow<List<PocketArticle>> = _articles

    fun loadArticles() {
        viewModelScope.launch {
            val result = pocketRepository.getItems()

            if (result is NetworkResult.Success) {
                _articles.value = result.data.list.map { it.value }
            } else {
                // Handle error here
            }
        }
    }
}