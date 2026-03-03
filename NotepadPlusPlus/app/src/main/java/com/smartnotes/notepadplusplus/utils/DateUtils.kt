package com.smartnotes.notepadplusplus.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatDate(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            ""
        }
    }

    fun formatDateTime(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            ""
        }
    }
}
