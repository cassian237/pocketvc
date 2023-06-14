package not.a.bug.pocketv.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import net.glxn.qrgen.android.QRCode
import not.a.bug.pocketv.R
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.RequestTokenResponse
import not.a.bug.pocketv.viewmodel.AuthViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.requestToken()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val requestTokenResult by viewModel.requestTokenResult.collectAsState()
        val displayQrCode by viewModel.displayQrCode.collectAsState(initial = "")

        when (requestTokenResult) {
            is NetworkResult.Success -> {
                // Handle success case
            }

            is NetworkResult.Error -> {
                DisplayErrorAndRetryButton(requestTokenResult, viewModel)
            }

            is NetworkResult.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        if (displayQrCode.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.auth_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.auth_subtitle),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))
                var visibility by remember { mutableStateOf(false) }
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    visible = visibility,
                    enter = fadeIn(tween(2000)),
                    exit = fadeOut()
                ) {
                    Image(
                        bitmap = QRCode.from(displayQrCode).bitmap().asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                visibility = true
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DisplayErrorAndRetryButton(
    requestTokenResult: NetworkResult<RequestTokenResponse>,
    viewModel: AuthViewModel
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val error = (requestTokenResult as NetworkResult.Error).exception.message
        Text(
            text = "Erreur de demande du jeton de requête : $error",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = { viewModel.requestToken() }) {
            Text(text = "Se connecter avec Pocket")
        }
    }
}