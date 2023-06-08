package not.a.bug.pocketv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.PocketUser
import not.a.bug.pocketv.model.RequestTokenResponse
import not.a.bug.pocketv.repository.PocketRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val pocketRepository: PocketRepository
) : ViewModel() {

    private val _requestTokenResult = MutableStateFlow<NetworkResult<RequestTokenResponse>?>(null)
    val requestTokenResult: StateFlow<NetworkResult<RequestTokenResponse>?> = _requestTokenResult

    private val _authorizeResult = MutableStateFlow<NetworkResult<PocketUser>?>(null)
    val authorizeResult: StateFlow<NetworkResult<PocketUser>?> = _authorizeResult

    private val _openWebViewEvent = MutableSharedFlow<String>()
    val openWebViewEvent: SharedFlow<String> = _openWebViewEvent

    fun requestToken() {
        viewModelScope.launch {
            _requestTokenResult.value = pocketRepository.requestToken("pocketapp107637")
            val requestTokenResult = _requestTokenResult.value

            if (requestTokenResult is NetworkResult.Success) {
                val requestToken = requestTokenResult.data.code
                openWebView("https://getpocket.com/auth/authorize?request_token=${requestToken}&redirect_uri=pocketapp107637I")

            }
        }
    }

    // Appelé lorsque l'utilisateur revient à l'application après l'autorisation Pocket
    fun handleAuthorizationCallback(code: String) {
        viewModelScope.launch {
            _authorizeResult.value = pocketRepository.authorize(code)
        }
    }

    private fun openWebView(url: String) {
        viewModelScope.launch {
            _openWebViewEvent.emit(url)
        }
    }
}
