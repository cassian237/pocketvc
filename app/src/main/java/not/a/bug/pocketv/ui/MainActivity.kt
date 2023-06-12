package not.a.bug.pocketv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.foundation.ExperimentalTvFoundationApi
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRow
import androidx.tv.material3.TabRowDefaults
import androidx.tv.material3.Text
import dagger.hilt.android.AndroidEntryPoint
import not.a.bug.notificationcenter.util.FocusGroup
import not.a.bug.notificationcenter.util.PremiumPillIndicator
import not.a.bug.pocketv.ui.component.AuthScreen
import not.a.bug.pocketv.ui.component.HomeScreen
import not.a.bug.pocketv.ui.component.SearchScreen
import not.a.bug.pocketv.ui.component.SettingsScreen
import not.a.bug.pocketv.ui.theme.PocketvTheme
import not.a.bug.pocketv.viewmodel.AuthViewModel
import not.a.bug.pocketv.viewmodel.HomeViewModel

@OptIn(ExperimentalTvFoundationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var selectedTab by mutableStateOf(0)

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isPremium = false
            val tabs = listOf("Search", "Home", "Settings")
            var selectedTabIndex by remember { mutableStateOf(1) }
            var isTabRowFocused by remember { mutableStateOf(false) }
            val topBarFocusRequesters = List(size = 4) { FocusRequester() }
            val navController = rememberNavController()
            val authState by authViewModel.authState.collectAsState()
            var isTopBarVisible by remember { mutableStateOf(true) }

            PocketvTheme {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    LaunchedEffect(authViewModel.authState, selectedTabIndex) {
                        authViewModel.authState.collect { authState ->
                            if (authState.accessToken == null) {
                                navController.navigate("authScreen")
                            } else {
                                topBarFocusRequesters[selectedTabIndex].requestFocus()
                                when (selectedTabIndex) {
                                    0 -> navController.navigate("search")
                                    1 -> navController.navigate("home")
                                    2 -> navController.navigate("settings")
                                    3 -> navController.navigate("settings")
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()

                            .onFocusChanged {
                                isTabRowFocused = it.isFocused || it.hasFocus
                            }
                    ) {
                        FocusGroup(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            // Only show TabRow when user is authenticated
                            if (authState.accessToken != null) {
                                TabRow(
                                    modifier = Modifier
                                        .padding(vertical = 16.dp),
                                    selectedTabIndex = selectedTabIndex,
                                    separator = { Spacer(modifier = Modifier.width(12.dp)) },
                                    indicator = @Composable { tabPositions ->
                                        tabPositions.getOrNull(selectedTabIndex)?.let {
                                            if (selectedTabIndex == 3) {
                                                PremiumPillIndicator(
                                                    currentTabPosition = it,
                                                    anyTabFocused = isTabRowFocused
                                                )
                                            } else {
                                                TabRowDefaults.PillIndicator(currentTabPosition = it)
                                            }
                                        }
                                    }
                                ) {
                                    tabs.forEachIndexed { index, tab ->
                                        Tab(
                                            modifier = Modifier
                                                .restorableFocus()
                                                .height(32.dp)
                                                .focusRequester(topBarFocusRequesters[index]),
                                            selected = index == selectedTabIndex,
                                            onFocus = { selectedTabIndex = index },
                                        ) {
                                            Text(
                                                text = tab,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }
                                    if (!isPremium) {
                                        Tab(
                                            modifier = Modifier
                                                .restorableFocus()
                                                .height(32.dp)
                                                .focusRequester(topBarFocusRequesters[3]),
                                            colors = TabDefaults.pillIndicatorTabColors(
                                                activeContentColor = MaterialTheme.colorScheme.onBackground,
                                                focusedContentColor = MaterialTheme.colorScheme.onSurface
                                            ),

                                            selected = 3 == selectedTabIndex,
                                            onFocus = { selectedTabIndex = 3 },
                                        ) {
                                            Text(
                                                text = "Premium \uD83D\uDE80",
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        NavHost(navController, startDestination = "authScreen") {
                            composable("authScreen") { AuthScreen(authViewModel) }
                            composable("home") { HomeScreen(homeViewModel) }
                            composable("search") { SearchScreen(homeViewModel) { isTopBarVisible = it } }
                            composable("settings") { SettingsScreen() }
                        }
                    }
                }
            }
        }
    }
}