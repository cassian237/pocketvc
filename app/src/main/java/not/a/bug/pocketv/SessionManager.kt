package not.a.bug.pocketv

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import not.a.bug.pocketv.viewmodel.AuthViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(context: Context) {

    companion object {
        private const val SECURE_PREFS_NAME = "secure_prefs"
        private const val ACCESS_TOKEN = "accessToken"
    }

    data class UserState(
        val accessToken: String?
    )

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            SECURE_PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    private val _authState = MutableStateFlow(UserState(accessToken = getAccessToken()))
    val authState: StateFlow<UserState> = _authState

    fun saveAccessToken(token: String?) {
        encryptedSharedPreferences.edit().putString(ACCESS_TOKEN, token).apply()
        _authState.value = UserState(accessToken = token)
    }

    fun getAccessToken(): String? {
        return encryptedSharedPreferences.getString(ACCESS_TOKEN, null)
    }
    fun getConsumerKey() = "107637-fd094be8009e60ad014b7d0"

    fun clear() {
        encryptedSharedPreferences.edit().clear().apply()
    }

    fun logOut() {
        saveAccessToken(null)
        _authState.value = UserState(null)
    }
}