package not.a.bug.pocketv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import not.a.bug.pocketv.ui.component.AuthScreen
import not.a.bug.pocketv.ui.component.HomeScreen
import not.a.bug.pocketv.ui.theme.PocketvTheme
import not.a.bug.pocketv.viewmodel.AuthViewModel
import not.a.bug.pocketv.viewmodel.HomeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PocketvTheme {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)) {
                    // Create a NavHost with the NavController
                    val navController = rememberNavController()

                    // Observe authentication state and navigate accordingly
                    LaunchedEffect(authViewModel.authState) {
                        authViewModel.authState.collect { authState ->
                            if (authState.accessToken == null) {
                                navController.navigate("authScreen")
                            } else {
                                navController.navigate("home")
                            }
                        }
                    }

                    // Setup the NavHost and its NavGraph
                    NavHost(navController, startDestination = "authScreen") {
                        composable("authScreen") { AuthScreen(authViewModel) }
                        composable("home") { HomeScreen(homeViewModel) }
                    }
                }
            }
        }
    }
}