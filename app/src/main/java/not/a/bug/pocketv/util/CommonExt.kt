package not.a.bug.notificationcenter.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun String.withBoldText(): AnnotatedString {
    // Expression régulière pour détecter les balises <b> et </b> et leur contenu
    val pattern = "<b>(.*?)</b>".toRegex()

    val parts = pattern.findAll(this)

    val boldPortions = parts.map { it.groupValues[1] }.toList()

    return buildAnnotatedString {
        var currentText = this@withBoldText
        boldPortions.forEach { boldPortion ->
            val startIndex = currentText.indexOf("<b>$boldPortion</b>")
            if (startIndex >= 0) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(currentText.substring(0, startIndex))
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(boldPortion)
                }
                currentText = currentText.substring(startIndex + boldPortion.length + 7)
            }
        }
        append(currentText)
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}