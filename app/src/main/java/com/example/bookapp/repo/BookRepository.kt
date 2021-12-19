package com.example.bookapp.repo

import com.example.bookapp.network.BookService
import javax.inject.Inject

class BookRepository @Inject constructor(private val bookService: BookService) {

    suspend fun getBook(query : String) = bookService.getBookFromApi(query)

}