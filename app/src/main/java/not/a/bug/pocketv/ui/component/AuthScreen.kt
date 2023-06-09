package not.a.bug.pocketv.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import net.glxn.qrgen.android.QRCode
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
                CircularProgressIndicator()
            }
        }

        if (displayQrCode.isNotEmpty()) {
            Image(
                bitmap = QRCode.from(displayQrCode).bitmap().asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
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
        Text(text = "Erreur de demande du jeton de requête : $error", modifier = Modifier.padding(bottom = 16.dp))
        Button(onClick = { viewModel.requestToken() }) {
            Text(text = "Se connecter avec Pocket")
        }
    }
}