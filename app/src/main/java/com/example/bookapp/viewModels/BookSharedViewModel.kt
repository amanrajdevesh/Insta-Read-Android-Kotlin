package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.modals.Item
import com.example.bookapp.modals.VolumeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookSharedViewModel @Inject constructor() : ViewModel(){

    val volume = MutableLiveData<VolumeInfo>()

    val post = MutableLiveData<Post>()

    val item = MutableLiveData<Item>()

    fun sendBook(book : VolumeInfo){
        volume.value = book
    }

    fun sendUser(currentPost : Post){
        post.value = currentPost
    }

    fun sendItem(currentItem : Item){
        item.value = currentItem
    }

}