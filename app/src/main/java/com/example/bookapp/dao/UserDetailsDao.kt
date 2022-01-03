package com.example.bookapp.dao

import com.example.bookapp.firebaseModals.UserDetails
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserDetailsDao @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val detailCollection = db.collection("details")
    val auth = FirebaseAuth.getInstance()

    fun updateFavourite() {
        val userDetails = UserDetails()
        detailCollection.document(auth.currentUser!!.uid).set(userDetails)
    }

    fun getUserDetails(uid : String) : Task<DocumentSnapshot> {
        return detailCollection.document(uid).get()
    }

}