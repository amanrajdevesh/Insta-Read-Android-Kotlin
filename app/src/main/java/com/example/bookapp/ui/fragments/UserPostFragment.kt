package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.OnPostClickedListener
import com.example.bookapp.adapters.PostAdapter
import com.example.bookapp.adapters.PostFeedAdapter
import com.example.bookapp.databinding.FragmentUserPostBinding
import com.example.bookapp.firebaseModals.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UserPostFragment : Fragment() {

    lateinit var binding : FragmentUserPostBinding
    lateinit var adapter : PostFeedAdapter
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_post,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        val db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val postCollection = db.collection("posts")
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo("user.uid",auth.currentUser!!.uid)
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
            Log.v("InstaRead" , "${post.size}")
            adapter.addPost(post)
        }

    }

    private fun setUpRecyclerView() {
        adapter = PostFeedAdapter()
        binding.userPostRecycler.layoutManager = LinearLayoutManager(activity)
        binding.userPostRecycler.adapter = adapter
    }

}