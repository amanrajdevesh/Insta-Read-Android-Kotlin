package com.example.bookapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.modals.Item
import com.example.bookapp.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(val repository: BookRepository) : ViewModel(){

    val bookList : MutableLiveData<List<Item>> = MutableLiveData()

    fun getNews(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getBook(query)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    bookList.value = response.body()!!.items
                }else {
                    Log.d("aman raj devesh" , "Error fetching data")
                }
            }
        }
    }


}