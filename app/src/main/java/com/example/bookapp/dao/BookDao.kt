package com.example.bookapp.dao

import com.example.bookapp.firebaseModals.BookFirebase
import com.example.bookapp.firebaseModals.User
import com.example.bookapp.modals.Book
import com.example.bookapp.modals.VolumeInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class BookDao {

    private val db = FirebaseFirestore.getInstance()
    private val bookCollection = db.collection("books")
    private val auth = FirebaseAuth.getInstance()

    fun addBook(book: VolumeInfo){
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = UserDao()
            val user = userDao.getUser(auth.currentUser!!.uid).await().toObject(User::class.java)
            book.addUser(user)
            bookCollection.document().set(book)
        }
    }

    private fun getBookById(postId : String) : Task<DocumentSnapshot> {
        return bookCollection.document(postId).get()
    }

    suspend fun getBook(postId : String) : VolumeInfo {
        return CoroutineScope(Dispatchers.IO).async {
            getBookById(postId).await().toObject(VolumeInfo::class.java)!!
        }.await()
    }

}