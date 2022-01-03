package com.example.bookapp.dao

import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.firebaseModals.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostDao @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()
    private val postCollection = db.collection("posts")
    private val auth = FirebaseAuth.getInstance()

    fun addPost(post: Post){
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = UserDao()
            val user = userDao.getUser(auth.currentUser!!.uid).await().toObject(User::class.java)!!
            post.addUser(user)
            postCollection.document().set(post)
        }
    }

    private fun getPostById(postId : String) : Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }

    suspend fun getPost(postId : String) : Post {
        return CoroutineScope(Dispatchers.IO).async {
            getPostById(postId).await().toObject(Post::class.java)!!
        }.await()
    }
}