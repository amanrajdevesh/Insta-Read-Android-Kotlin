package com.example.bookapp.firebaseModals

class Post(val name : String = "",
           val author : String? = "",
           val category: String = "",
           val imageUrl : String = "",
           val body : String = "",
           val read : String = "",
           val createdAt : Long = 0L,
          var uid : String = ""
           //var user: User = User()
){

    fun addUserUid(uid : String) {
        this.uid = uid
    }

    /*
    fun addUser(user: User) {
        this.user = user
    }

     */


}
