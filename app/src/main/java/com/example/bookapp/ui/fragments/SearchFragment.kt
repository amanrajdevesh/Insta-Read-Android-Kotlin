package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import com.example.bookapp.dao.UserDetailsDao
import com.example.bookapp.databinding.FragmentSearchBinding
import com.example.bookapp.firebaseModals.User
import com.example.bookapp.modals.Item
import com.example.bookapp.utils.Resource
import com.example.bookapp.viewModels.BookViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.app.Activity
import android.app.AlertDialog
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() , SearchAdapterInterface{

    lateinit var binding: FragmentSearchBinding
    private val viewModel : BookViewModel by viewModels()
    lateinit var mAdapter : SearchAdapter
    @Inject lateinit var bookDao: BookDao
    @Inject lateinit var userDao: UserDao
    @Inject lateinit var userDetailsDao: UserDetailsDao
    @Inject
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_search , container , false)
        setHasOptionsMenu(true)
       // binding.searchToolbar.inflateMenu(R.menu.search_menu)
        //binding.searchToolbar.setOnMenuItemClickListener {
          //  if(it.itemId==R.id.searchApi){
            //    Toast.makeText(activity,"Search",Toast.LENGTH_SHORT).show()
           // }
            //true
        //}
        //setSupportActionBar(binding.searchToolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRecyclerView()
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = activity?.layoutInflater
        builder.setView(inflater?.inflate(R.layout.item_progress,null))
        val alertDialog = builder.create()
        //auth = FirebaseAuth.getInstance()
        //userDao = UserDao()
        //bookDao = BookDao()
        //userDetailsDao = UserDetailsDao()

        binding.searchButton.setOnClickListener {
            val name = binding.etBook.text.toString()
            viewModel.getNews(name)
            hideKeyboard(requireActivity())
        }

        viewModel.bookList.observe(viewLifecycleOwner , Observer { resource ->
            when(resource) {
                is Resource.Success -> {
                    alertDialog.dismiss()
                    resource.data?.let { mAdapter.addAllItems(it) }
                }
                is Resource.Error -> {
                    alertDialog.dismiss()
                    Log.d("aman raj devesh" , "${resource.msg}")
                }
                is Resource.Loading -> {
                    alertDialog!!.show()
                }
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
                if (x != null) {
                    userDao.updateLibrary(x)
                }
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
                if (x != null) {
                    userDao.updateFavourite(x)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
        if (menu != null) {
            menu.removeItem(R.id.logout);
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.searchApi -> Toast.makeText(activity,"Search",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPostButtonClicked(item: Item) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToPostFragment(item))
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}