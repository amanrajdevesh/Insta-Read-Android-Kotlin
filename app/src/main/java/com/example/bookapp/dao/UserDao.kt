package com.example.bookapp.dao

import com.example.bookapp.firebaseModals.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDao @Inject constructor(){

    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    private val auth = FirebaseAuth.getInstance()

    fun addUser(user : User?){
        user?.let {
            userCollection.document(auth.currentUser!!.uid).set(user)
        }
    }


    fun updateLibrary(x : Int?){
        val userRef = userCollection.document(auth.currentUser!!.uid)
        userRef.update("library" , x)
    }

    fun updateFavourite(x : Int?){
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = UserDao()
            val user = userDao.getUser(auth.currentUser!!.uid).await().toObject(User::class.java)
            user?.favourites = user?.favourites?.plus(1)!!
            userCollection.document(auth.currentUser!!.uid).set(user)
        }

       // userRef.update("favourites" , x)
    }



    fun getUser(uid: String) : Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }


}