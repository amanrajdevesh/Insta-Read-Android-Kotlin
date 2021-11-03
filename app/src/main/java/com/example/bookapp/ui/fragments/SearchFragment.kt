package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.SearchAdapter
import com.example.bookapp.adapters.SearchAdapterInterface
import com.example.bookapp.dao.BookDao
import com.example.bookapp.dao.UserDao
import com.example.bookapp.databinding.FragmentSearchBinding
import com.example.bookapp.firebaseModals.BookFirebase
import com.example.bookapp.firebaseModals.User
import com.example.bookapp.modals.Item
import com.example.bookapp.viewModels.BookViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : Fragment() , SearchAdapterInterface{

    lateinit var binding: FragmentSearchBinding
    private val viewModel : BookViewModel by viewModels()
    lateinit var mAdapter : SearchAdapter
    lateinit var bookDao: BookDao
    lateinit var userDao: UserDao
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_search , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRecyclerView()

        auth = FirebaseAuth.getInstance()
        userDao = UserDao()
        bookDao = BookDao()

        binding.searchButton.setOnClickListener {
            val name = binding.etBook.text.toString()
            viewModel.getNews(name)
        }

        viewModel.bookList.observe(viewLifecycleOwner , Observer { list ->
            list?.let {
                mAdapter.addAllItems(it)
            }
        })
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        mAdapter = SearchAdapter(this)
        recyclerView.adapter = mAdapter
    }

    override fun onLibraryButtonClicked(item: Item) {
        val book = item.volumeInfo
        bookDao.addBook(book)
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUser(auth.currentUser!!.uid).await().toObject(User::class.java)
            withContext(Dispatchers.Main){
                var x = user?.library
                x = x?.plus(1)
                userDao.updateLibrary(x)
            }
        }
    }

    override fun onAddLaterButtonClicked(item: Item) {
        val book = item.volumeInfo
        book.readLater = true
        bookDao.addBook(book)
    }

    override fun onFavouriteButtonClicked(item: Item) {
        val book = item.volumeInfo
        book.favourite = true
        bookDao.addBook(book)
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUser(auth.currentUser!!.uid).await().toObject(User::class.java)
            withContext(Dispatchers.Main){
                var x = user?.favourites
                x = x?.plus(1)
                userDao.updateFavourite(x)
            }
        }
    }

    override fun onPostButtonClicked(item: Item) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToPostFragment(item))
    }

}