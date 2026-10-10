package dz.plombidz.app.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val BrandPrimary = Color(0xFF0B6FA4)
private val BrandSecondary = Color(0xFF00A0B0)

private val PlombiLight = lightColorScheme(
    primary = BrandPrimary,
    secondary = BrandSecondary,
)

private val PlombiDark = darkColorScheme(
    primary = Color(0xFF7CC9EA),
    secondary = Color(0xFF5ED6E2),
)

/**
 * DZ-2026 app theme: system dark mode + Material You dynamic color on
 * Android 12+, static brand seed below that. Offline, no dependencies.
 */
@Composable
fun PlombiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val scheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> PlombiDark
        else -> PlombiLight
    }
    MaterialTheme(colorScheme = scheme, content = content)
}
