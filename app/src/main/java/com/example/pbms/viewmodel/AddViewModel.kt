package com.example.pbms.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbms.model.data.Book
import com.example.pbms.model.data.BookDao
import com.example.pbms.model.data.DatabaseInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = DatabaseInstance.getDatabase(application).bookDao()
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    fun addBook(
        title: String,
        author: String,
        genre: String?,
        dateAdded: Long,
        currentProgress: String,
        totalPages: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {

            // user input will be String. String to Int/Null. These are for progress calculation
            // we need total pages to be minimum 1 for the filter to work later on
            var totalPages = totalPages.toIntOrNull()?: 1

            // user is not allowed to have 0 pages total :)
            if (totalPages == 0){
                totalPages = 1
            }
            val currentProgress = currentProgress.toIntOrNull() ?: 0

            val book = Book(
                title = title.trim(),
                author = author.trim(),
                genre = genre.takeIf { !it.isNullOrBlank() },
                totalPages = totalPages,
                currentProgress = currentProgress.coerceAtMost(totalPages), //progress is <= totalPages
                dateAdded = dateAdded
            )
            dao.insertBook(book)
            println(currentProgress)

            onSuccess() // pop back stack

        }
    }
}
