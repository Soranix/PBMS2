package com.example.pbms.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbms.model.data.AppDatabase
import com.example.pbms.model.data.Book
import com.example.pbms.model.data.BookDao
import com.example.pbms.model.data.DatabaseInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Core = Implement basic crud functionality

class HomeViewModel (application: Application) : AndroidViewModel(application){

    /*
    - UI observes 'books', read only
    - '_books' can be altered by the viewmodel
    - stateflow will not retain data after the application closes.
    */

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    // database instantiated
    private val dao = DatabaseInstance.getDatabase(application).bookDao()


    // this function will help load and refresh the database that the user will see

    // read
    fun loadBooks() {
        viewModelScope.launch {
            _books.value = dao.getAllBooks()
        }
    }
    fun loadUnreadBooks() {
        viewModelScope.launch {
            _books.value = dao.getUnreadBooks()
        }
    }

    /*
    suspend fun getBookById(id: Int): Book? {
        return dao.getBookById(id)
    }

     */
}
