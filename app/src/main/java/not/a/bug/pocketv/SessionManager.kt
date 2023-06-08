package not.a.bug.pocketv

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val context: Context) {

    companion object {
        private const val SECURE_PREFS_NAME = "secure_prefs"
        private const val ACCESS_TOKEN = "accessToken"
    }

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

    fun saveAccessToken(token: String) {
        encryptedSharedPreferences.edit().putString(ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return encryptedSharedPreferences.getString(ACCESS_TOKEN, null)
    }
    fun getConsumerKey() = "107637-fd094be8009e60ad014b7d0"

    fun clear() {
        encryptedSharedPreferences.edit().clear().apply()
    }
}