package not.a.bug.pocketv.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import not.a.bug.pocketv.R
import not.a.bug.pocketv.ui.theme.JetStreamCardShape

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(
) {
    val focusRequester = remember { FocusRequester() }
    var isFirstPopulation by rememberSaveable { mutableStateOf(true) }
    var isDarkTheme = remember { mutableStateOf(true) }

    Row() {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(id = R.string.settings_subtitle),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        TvLazyColumn(modifier = Modifier
            .weight(1f)
            .focusRequester(focusRequester),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                item { Spacer(modifier = Modifier.height(32.dp)) }
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        shape = ClickableSurfaceDefaults.shape(shape = JetStreamCardShape),
                        color = ClickableSurfaceDefaults.color(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.8f
                            )
                        ),
                        onClick = {
                            isDarkTheme.value = !isDarkTheme.value
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = stringResource(id = R.string.color_theme),
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Switch(
                                checked = isDarkTheme.value,
                                onCheckedChange = {  },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                }
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        shape = ClickableSurfaceDefaults.shape(shape = JetStreamCardShape),
                        color = ClickableSurfaceDefaults.color(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.8f
                            )
                        ),
                        onClick = {
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = stringResource(id = R.string.log_out),
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            })
    }
}