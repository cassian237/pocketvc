package not.a.bug.pocketv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import not.a.bug.pocketv.SessionManager
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.PocketUser
import not.a.bug.pocketv.model.RequestTokenResponse
import not.a.bug.pocketv.repository.PocketRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val pocketRepository: PocketRepository,
    private val db: FirebaseFirestore,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow(UserState(accessToken = sessionManager.getAccessToken()))
    val authState: StateFlow<UserState> = _authState

    private val _requestTokenResult = MutableStateFlow<NetworkResult<RequestTokenResponse>>(NetworkResult.Loading)
    val requestTokenResult: StateFlow<NetworkResult<RequestTokenResponse>> = _requestTokenResult

    private val _displayQrCode = MutableSharedFlow<String>()
    val displayQrCode: SharedFlow<String> = _displayQrCode

    fun requestToken() {
        viewModelScope.launch {
            val deviceId = UUID.randomUUID().toString()
            _requestTokenResult.value = pocketRepository.requestToken("https://pocketv-6ebcf.web.app?code=$deviceId")
            val requestTokenResult = _requestTokenResult.value

            if (requestTokenResult is NetworkResult.Success) {
                val requestToken = requestTokenResult.data.code

                _displayQrCode.emit("https://pocketv-6ebcf.web.app?request_token=${requestToken}&key=$deviceId")
                listenToAuthSuccess(deviceId, requestToken)
            }
        }
    }

    private fun listenToAuthSuccess(id: String, requestToken: String) {
        db.collection("codes").document(id)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.exists()) {
                    val authorized = snapshot.getBoolean("value")
                    if (authorized == true) {
                        handleAuthorizationCallback(requestToken)
                        return@addSnapshotListener
                    }
                }
            }
    }

    // Appelé lorsque l'utilisateur revient à l'application après l'autorisation Pocket
    private fun handleAuthorizationCallback(code: String) {
        viewModelScope.launch {
            val result = pocketRepository.authorize(code)

            if (result is NetworkResult.Success) {
                _authState.value = UserState(accessToken = sessionManager.getAccessToken())
            }
        }
    }

    data class UserState(
        val accessToken: String?
    )
}
