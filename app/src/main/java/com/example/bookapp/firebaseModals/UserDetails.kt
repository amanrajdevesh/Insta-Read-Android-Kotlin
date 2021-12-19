package com.example.bookapp.firebaseModals

class UserDetails(
    val post : Int = 0 ,
    val favourite : Int = 0 ,
    val library : Int = 0 ,
    var user: User = User()
){
    fun addUser(user: User) {
        this.user = user
    }
}
