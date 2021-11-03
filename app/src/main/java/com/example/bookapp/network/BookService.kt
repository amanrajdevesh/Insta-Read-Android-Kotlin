package com.example.bookapp.network

import com.example.bookapp.modals.Book
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "AIzaSyDJWWm5UWc9rj_4N5km7utn_EBO10KZr0k"
interface BookService {

    @GET("/books/v1/volumes?${API_KEY}")
    suspend fun getBookFromApi(@Query("q") query : String) : Response<Book>

}

//"https://www.googleapis.com/books/v1/volumes?q=elon&api=AIzaSyDJWWm5UWc9rj_4N5km7utn_EBO10KZr0k"