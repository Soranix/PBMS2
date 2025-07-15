package com.example.pbms.model.data

import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.Locale


@Entity(tableName = "books")
data class Book (
@PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String?,
    val dateAdded: Long = System.currentTimeMillis(),
    val currentProgress: Int, // current pages
    val totalPages: Int // total pages
) {
    // custom getter to calculate percentage
    val percentage: Int
        get() = if (totalPages > 0) {
            (currentProgress * 100) / totalPages
        } else 0


    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
