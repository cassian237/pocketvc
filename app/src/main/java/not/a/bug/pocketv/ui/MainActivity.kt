package not.a.bug.pocketv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import not.a.bug.pocketv.ui.component.AuthScreen
import not.a.bug.pocketv.ui.component.HomeScreen
import not.a.bug.pocketv.ui.theme.PocketvTheme
import not.a.bug.pocketv.viewmodel.AuthViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PocketvTheme {
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
                    composable("home") { /* HomeScreen() */ }
                }
            }
        }
    }
}