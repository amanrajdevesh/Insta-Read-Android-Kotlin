package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.OnPostClickedListener
import com.example.bookapp.adapters.PostFeedAdapter
import com.example.bookapp.databinding.FragmentPostFeedBinding
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.viewModels.BookSharedViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostFeedFragment : Fragment(), OnPostClickedListener {

    private lateinit var adapter : PostFeedAdapter
    lateinit var binding : FragmentPostFeedBinding
//    lateinit var viewModel: BookSharedViewModel
    private val viewModel: BookSharedViewModel by activityViewModels()
    @Inject lateinit var db : FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_feed,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        //val db = FirebaseFirestore.getInstance()
        val postCollection = db.collection("posts")
       // viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        viewModel.post.observe(viewLifecycleOwner, Observer {
            val uid = it.user.uid
            val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
                .whereEqualTo("user.uid",uid)
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
                adapter.addPost(post)
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = PostFeedAdapter(this)
        binding.userPostRecycler.layoutManager = LinearLayoutManager(activity)
        binding.userPostRecycler.adapter = adapter
    }

    override fun onPostClicked(post: Post) {

    }
}