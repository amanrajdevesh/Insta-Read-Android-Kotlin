package com.example.bookapp.firebaseModals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val uid : String = "",
    val name : String = "",
    val imageUrl : String = "",
    val category: ArrayList<String>? = null,
    val post : Int = 0,
    val library : Int = 0,
    var favourites : Int = 0
): Parcelable