package com.example.bookapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.modals.Item
import com.example.bookapp.repo.BookRepository
import com.example.bookapp.ui.MainActivity
import com.example.bookapp.utils.LoadingDialog
import com.example.bookapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepository) : ViewModel(){

    val bookList : MutableLiveData<Resource<List<Item>>> = MutableLiveData()

    fun getNews(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            bookList.postValue(Resource.Loading())
            val response = repository.getBook(query)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    bookList.postValue(Resource.Success(response.body()!!.items))
                }else {
                    bookList.postValue(Resource.Error(response.message()))
                }
            }
        }
    }


}