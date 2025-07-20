package com.example.pbms.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbms.model.data.Book
import com.example.pbms.model.data.DatabaseInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = DatabaseInstance.getDatabase(application).bookDao()

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    fun loadBook(id: Int) {
        viewModelScope.launch {
            _book.value = dao.getBookById(id)
        }
    }

    // update book and navigate back
    fun updateBook(updatedBook: Book, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.updateBook(updatedBook)
            onSuccess()
        }
    }

    // delete book and navigate back
    fun deleteBook(book: Book, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.deleteBook(book)
            onSuccess()
        }
    }
}