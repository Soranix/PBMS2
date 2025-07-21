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

            val totalPages = totalPages.toIntOrNull()?: 0
            val currentProgress = currentProgress.toIntOrNull() ?: 0

            val book = Book(
                title = title.trim(),
                author = author.trim(),
                genre = genre.takeIf { !it.isNullOrBlank() },
                currentProgress = currentProgress.coerceAtMost(totalPages), //progress is <= totalPages
                totalPages = totalPages,
                dateAdded = dateAdded
            )
            dao.insertBook(book)

            onSuccess() // pop back stack

        }
        fun loadBooks() {
            viewModelScope.launch {
                _books.value = dao.getAllBooks()
            }
        }

        fun updateBook(book: Book) {
            viewModelScope.launch {
                dao.updateBook(book)
                loadBooks()
            }
        }
    }
}
