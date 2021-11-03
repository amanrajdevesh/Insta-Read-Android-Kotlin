package com.example.bookapp.firebaseModals

class BookFirebase(
    val title : String = "",
    val author : List<String>? = null,
    val categories: List<String> = ArrayList<String>(),
    val imageUrl : String = "",
    val language : String = "",
    val description : String = "",
    val pageCount : Int = 0,
    val favourite : Boolean = false,
    val readLater : Boolean = false,
    var user : User? = User()
){
    fun addUser(user: User?) {
        this.user = user
    }
}
