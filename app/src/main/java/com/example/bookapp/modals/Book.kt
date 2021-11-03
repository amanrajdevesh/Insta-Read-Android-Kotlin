package com.example.bookapp.modals

data class Book(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)