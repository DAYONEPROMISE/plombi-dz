package dz.plombidz.app.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/** Minimal FR/AR switch persisted on-device; applied before setContent. */
object LocaleHelper {
    private const val PREFS = "plombidz_prefs"
    private const val KEY = "lang"

    fun current(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, "fr") ?: "fr"

    fun set(context: Context, lang: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY, lang).apply()
    }

    fun apply(context: Context): Context {
        val locale = Locale(current(context))
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
