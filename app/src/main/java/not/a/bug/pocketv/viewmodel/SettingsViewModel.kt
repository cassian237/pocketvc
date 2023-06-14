package not.a.bug.pocketv.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import not.a.bug.pocketv.SessionManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val sessionManager: SessionManager

) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(
        sharedPreferences.getBoolean("isDarkTheme", true)
    )
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun toggleTheme() = viewModelScope.launch {
        sharedPreferences.edit().putBoolean("isDarkTheme", !_isDarkTheme.value).apply()
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun logOut() {
        sessionManager.logOut()
    }
}