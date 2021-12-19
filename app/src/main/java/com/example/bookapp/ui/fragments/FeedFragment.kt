package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.OnPostClickedListener
import com.example.bookapp.adapters.PostAdapter
import com.example.bookapp.dao.PostDao
import com.example.bookapp.databinding.FragmentFeedBinding
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.viewModels.BookSharedViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FeedFragment : Fragment() , OnPostClickedListener{

    lateinit var binding : FragmentFeedBinding
    lateinit var mAdapter : PostAdapter
    lateinit var viewModel: BookSharedViewModel
    lateinit var postDao: PostDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_feed , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val postCollection = db.collection("posts")
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val option = FirestoreRecyclerOptions.Builder<Post>().setQuery(query , Post::class.java).build()
        viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        postDao = PostDao()

        mAdapter = PostAdapter(option,this)
        binding.feedRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    override fun onPostClicked(postId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val post = async { postDao.getPost(postId) }
            viewModel.sendUser(post.await())
        }
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostUserFragment())
        /*
        val bottomSheetDialog = UserBottomSheetDialog()
        fragmentManager?.let { bottomSheetDialog.show(it,bottomSheetDialog.tag) }

         */
    }

}