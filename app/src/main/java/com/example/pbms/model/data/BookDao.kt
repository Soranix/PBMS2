package com.example.pbms.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY id ASC")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books WHERE title LIKE :title")
    suspend fun getBookByTitle(title: String): List<Book>

    @Query("SELECT * FROM books WHERE id LIKE :id")
    suspend fun getBookById(id: Int): Book

    // when you need to insert or overwrite existing data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("DELETE FROM books WHERE title = :title")
    suspend fun deleteBookByTitle(title: String)

    @Delete
    suspend fun deleteBook(book: Book)

    // for safe updates for existing data
    @Update
    suspend fun updateBook(book:Book)

    @Query("SELECT * FROM books WHERE currentProgress < totalPages ORDER BY id ASC")
    suspend fun getUnreadBooks(): List<Book>


}