package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.OnPostClickedListener
import com.example.bookapp.adapters.PostAdapter
import com.example.bookapp.adapters.PostFeedAdapter
import com.example.bookapp.dao.PostDao
import com.example.bookapp.databinding.FragmentFeedBinding
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.viewModels.BookSharedViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() , OnPostClickedListener{

    lateinit var binding : FragmentFeedBinding
    lateinit var mAdapter : PostFeedAdapter
    private val viewModel: BookSharedViewModel by activityViewModels()
//    lateinit var viewModel: BookSharedViewModel
    @Inject lateinit var postDao: PostDao
    @Inject lateinit var db : FirebaseFirestore

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
        //val db = FirebaseFirestore.getInstance()
        val postCollection = db.collection("posts")
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        query.addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("Aman", "Listen failed.", e)
                return@addSnapshotListener
            }

            val post = ArrayList<Post>()
            for (doc in value!!) {
                doc.toObject(Post::class.java)?.let { p ->
                    post.add(p)
                }
            }
            mAdapter.addPost(post)
        }
        //viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        //postDao = PostDao()

        mAdapter = PostFeedAdapter(this)
        binding.feedRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
    }


    override fun onPostClicked(post: Post) {
        viewModel.sendUser(post)
        val fragment = PostUserFragment()
        setCurrentFragment(fragment)
        //findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostUserFragment())
    }

    private fun setCurrentFragment(fragment : Fragment){
        val backStateName: String = fragment.javaClass.name
        val manager = activity?.supportFragmentManager
        val fragmentPopped = manager?.popBackStackImmediate(backStateName, 0)
        if(!fragmentPopped!!){
            manager.beginTransaction().apply {
                replace(R.id.container,fragment)
                addToBackStack(backStateName)
                commit()
            }
        }
    }

}