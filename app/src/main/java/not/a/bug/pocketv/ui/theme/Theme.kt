@file:OptIn(ExperimentalTvMaterial3Api::class)

package not.a.bug.pocketv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme
import not.a.bug.pocketv.R


private val DarkColorScheme @Composable get() = darkColorScheme(
    primary = colorResource(R.color.primary),
    onPrimary = colorResource(R.color.onPrimary),
    primaryContainer = colorResource(R.color.primaryContainer),
    onPrimaryContainer = colorResource(R.color.onPrimaryContainer),
    secondary = colorResource(R.color.secondary),
    onSecondary = colorResource(R.color.onSecondary),
    secondaryContainer = colorResource(R.color.secondaryContainer),
    onSecondaryContainer = colorResource(R.color.onSecondaryContainer),
    tertiary = colorResource(R.color.tertiary),
    onTertiary = colorResource(R.color.onTertiary),
    tertiaryContainer = colorResource(R.color.tertiaryContainer),
    onTertiaryContainer = colorResource(R.color.onTertiaryContainer),
    background = colorResource(R.color.background),
    onBackground = colorResource(R.color.onBackground),
    surface = colorResource(R.color.surface),
    onSurface = colorResource(R.color.onSurface),
    surfaceVariant = colorResource(R.color.surfaceVariant),
    onSurfaceVariant = colorResource(R.color.onSurfaceVariant),
    error = colorResource(R.color.error),
    onError = colorResource(R.color.onError),
    errorContainer = colorResource(R.color.errorContainer),
    onErrorContainer = colorResource(R.color.onErrorContainer),
    border = colorResource(R.color.border),
)

private val LightColorScheme @Composable get() = lightColorScheme(
    primary = colorResource(R.color.light_primary),
    onPrimary = colorResource(R.color.light_onPrimary),
    primaryContainer = colorResource(R.color.light_primaryContainer),
    onPrimaryContainer = colorResource(R.color.light_onPrimaryContainer),
    secondary = colorResource(R.color.light_secondary),
    onSecondary = colorResource(R.color.light_onSecondary),
    secondaryContainer = colorResource(R.color.light_secondaryContainer),
    onSecondaryContainer = colorResource(R.color.light_onSecondaryContainer),
    tertiary = colorResource(R.color.light_tertiary),
    onTertiary = colorResource(R.color.light_onTertiary),
    tertiaryContainer = colorResource(R.color.light_tertiaryContainer),
    onTertiaryContainer = colorResource(R.color.light_onTertiaryContainer),
    background = colorResource(R.color.light_background),
    onBackground = colorResource(R.color.light_onBackground),
    surface = colorResource(R.color.light_surface),
    onSurface = colorResource(R.color.light_onSurface),
    surfaceVariant = colorResource(R.color.light_surfaceVariant),
    onSurfaceVariant = colorResource(R.color.light_onSurfaceVariant),
    error = colorResource(R.color.light_error),
    onError = colorResource(R.color.light_onError),
    errorContainer = colorResource(R.color.light_errorContainer),
    onErrorContainer = colorResource(R.color.light_onErrorContainer),
    border = colorResource(R.color.light_border),
    inverseSurface = colorResource(R.color.light_inverse_surface_color),
)

@Composable
fun PocketvTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        shapes = MaterialTheme.shapes,
        typography = Typography,
        content = content
    )
}