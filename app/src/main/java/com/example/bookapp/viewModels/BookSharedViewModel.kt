package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.firebaseModals.User
import com.example.bookapp.modals.VolumeInfo
import com.example.bookapp.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class BookSharedViewModel : ViewModel(){

    val volume = MutableLiveData<VolumeInfo>()

    val post = MutableLiveData<Post>()

    fun sendBook(book : VolumeInfo){
        volume.value = book
    }

    fun sendUser(currentPost : Post){
        post.value = currentPost
    }

}