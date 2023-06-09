package not.a.bug.pocketv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import not.a.bug.pocketv.ui.component.AuthScreen
import not.a.bug.pocketv.ui.component.HomeScreen
import not.a.bug.pocketv.viewmodel.AuthViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "auth") {
                composable("auth") {
                    AuthScreen(authViewModel, navController)
                }
                composable("home") {
                    HomeScreen()
                }
            }
        }
    }
}