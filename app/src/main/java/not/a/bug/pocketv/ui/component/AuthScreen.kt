package not.a.bug.pocketv.ui.component

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.viewmodel.AuthViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel
) {
    val requestTokenResult by viewModel.requestTokenResult.collectAsState()
    val openWebViewEvent = viewModel.openWebViewEvent.collectAsState(null)
    val context = LocalContext.current

    when (requestTokenResult) {
        is NetworkResult.Success -> {
        }
        is NetworkResult.Error -> {
            val error = (requestTokenResult as NetworkResult.Error).exception.message
            Text(text = "Erreur de demande du jeton de requête : $error")
            Button(onClick = { viewModel.requestToken() }) {
                Text(text = "Se connecter avec Pocket")
            }
        }

        is NetworkResult.Loading -> {
            CircularProgressIndicator()
        }
        else -> {
            Button(onClick = { viewModel.requestToken() }) {
                Text(text = "Se connecter avec Pocket")
            }
        }
    }

    LaunchedEffect(openWebViewEvent.value) {
        val url = openWebViewEvent.value
        if (url != null) {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }
}
