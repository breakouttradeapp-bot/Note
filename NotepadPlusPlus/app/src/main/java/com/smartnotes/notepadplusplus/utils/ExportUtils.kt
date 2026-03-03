package com.smartnotes.notepadplusplus.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import java.io.File
import java.io.FileOutputStream

object ExportUtils {

    /** Share note text via Android share sheet */
    fun shareNoteText(context: Context, note: NoteEntity) {
        runCatching {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, note.title)
                putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Note"))
        }
    }

    /** Export as plain .txt */
    fun exportAsTxt(context: Context, note: NoteEntity) {
        runCatching {
            val fileName = safeFileName(note.title) + ".txt"
            val file = File(context.cacheDir, fileName)
            val text = buildString {
                appendLine(note.title)
                appendLine("=".repeat(note.title.length.coerceIn(5, 60)))
                appendLine()
                if (note.label.isNotBlank()) appendLine("[${note.label}]")
                appendLine()
                appendLine(note.content)
                appendLine()
                appendLine("─────────────────────")
                appendLine("Words: ${note.wordCount}")
                appendLine("Created: ${DateUtils.formatDateTime(note.createdAt)}")
                appendLine("Updated: ${DateUtils.formatDateTime(note.updatedAt)}")
            }
            file.writeText(text, Charsets.UTF_8)
            shareFile(context, file, "text/plain")
        }.onFailure { it.printStackTrace() }
    }

    /** Export as PDF using Android's built-in PdfDocument (API 19+) */
    fun exportAsPdf(context: Context, note: NoteEntity) {
        runCatching {
            val fileName = safeFileName(note.title) + ".pdf"
            val file = File(context.cacheDir, fileName)

            val pdfDoc = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 pts
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas

            // Paints
            val titlePaint = Paint().apply {
                textSize = 22f
                color = Color.parseColor("#6D28D9")
                isFakeBoldText = true
                isAntiAlias = true
            }
            val labelPaint = Paint().apply {
                textSize = 13f
                color = Color.parseColor("#6D28D9")
                isAntiAlias = true
            }
            val bodyPaint = Paint().apply {
                textSize = 13f
                color = Color.BLACK
                isAntiAlias = true
            }
            val metaPaint = Paint().apply {
                textSize = 11f
                color = Color.GRAY
                isAntiAlias = true
            }
            val dividerPaint = Paint().apply {
                color = Color.parseColor("#6D28D9")
                strokeWidth = 1.5f
            }

            val margin = 40f
            val maxWidth = 515f
            var y = 60f

            // Title
            canvas.drawText(note.title.take(70), margin, y, titlePaint)
            y += 28f

            // Divider
            canvas.drawLine(margin, y, margin + maxWidth, y, dividerPaint)
            y += 18f

            // Label
            if (note.label.isNotBlank()) {
                canvas.drawText("[${note.label}]", margin, y, labelPaint)
                y += 18f
            }

            y += 6f

            // Body — simple word-wrap
            val words = note.content.split(Regex("\\s+"))
            val lineBuffer = StringBuilder()
            for (word in words) {
                if (y > 790f) break
                val testLine = if (lineBuffer.isEmpty()) word else "$lineBuffer $word"
                if (bodyPaint.measureText(testLine) < maxWidth) {
                    lineBuffer.clear(); lineBuffer.append(testLine)
                } else {
                    canvas.drawText(lineBuffer.toString(), margin, y, bodyPaint)
                    y += 18f
                    lineBuffer.clear(); lineBuffer.append(word)
                }
            }
            if (lineBuffer.isNotEmpty() && y < 790f) {
                canvas.drawText(lineBuffer.toString(), margin, y, bodyPaint)
                y += 18f
            }

            // Footer
            y += 12f
            if (y < 800f) {
                canvas.drawLine(margin, y, margin + maxWidth, y, dividerPaint)
                y += 14f
                canvas.drawText("${note.wordCount} words  •  ${DateUtils.formatDateTime(note.updatedAt)}", margin, y, metaPaint)
            }

            pdfDoc.finishPage(page)
            FileOutputStream(file).use { pdfDoc.writeTo(it) }
            pdfDoc.close()

            shareFile(context, file, "application/pdf")
        }.onFailure { it.printStackTrace() }
    }

    private fun shareFile(context: Context, file: File, mimeType: String) {
        runCatching {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Export Note"))
        }
    }

    private fun safeFileName(name: String): String =
        name.trim()
            .replace(Regex("[^a-zA-Z0-9._\\- ]"), "_")
            .replace(" ", "_")
            .take(50)
            .ifBlank { "note_export" }
}
