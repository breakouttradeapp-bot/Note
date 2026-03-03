package com.smartnotes.notepadplusplus.utils

object RichTextUtils {

    fun countWords(text: String): Int {
        if (text.isBlank()) return 0
        return text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.size
    }

    fun estimateReadTime(wordCount: Int): String {
        val minutes = (wordCount / 200.0).coerceAtLeast(0.0)
        return when {
            minutes < 1.0 -> "< 1 min read"
            minutes < 1.5 -> "1 min read"
            else -> "${minutes.toInt()} min read"
        }
    }

    fun applyBold(text: String, start: Int, end: Int): String {
        val s = text.substring(0, start)
        val mid = text.substring(start, end)
        val e = text.substring(end)
        return "${s}**${mid}**${e}"
    }

    fun applyItalic(text: String, start: Int, end: Int): String {
        val s = text.substring(0, start)
        val mid = text.substring(start, end)
        val e = text.substring(end)
        return "${s}_${mid}_${e}"
    }

    fun markdownToHtml(md: String): String {
        var html = md
        // Bold
        html = html.replace(Regex("\\*\\*(.+?)\\*\\*"), "<b>$1</b>")
        // Italic
        html = html.replace(Regex("_(.+?)_"), "<i>$1</i>")
        // Bullet list
        html = html.replace(Regex("(?m)^- (.+)"), "• $1")
        // Newlines
        html = html.replace("\n", "<br/>")
        return html
    }

    fun htmlToPlainText(html: String): String {
        return android.text.Html.fromHtml(
            html,
            android.text.Html.FROM_HTML_MODE_LEGACY
        ).toString()
    }
}
