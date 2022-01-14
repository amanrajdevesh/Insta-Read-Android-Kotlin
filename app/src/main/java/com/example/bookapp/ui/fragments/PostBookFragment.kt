package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.adapters.LibraryAdapter
import com.example.bookapp.adapters.OnLibClickListener
import com.example.bookapp.dao.PostDao
import com.example.bookapp.databinding.FragmentPostBookBinding
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.modals.VolumeInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostBookFragment : Fragment(), OnLibClickListener {

    lateinit var binding : FragmentPostBookBinding
    @Inject
    lateinit var db : FirebaseFirestore
    @Inject lateinit var auth: FirebaseAuth
    lateinit var adapter : LibraryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_book,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        db.collection("books").whereEqualTo("user.uid",auth.currentUser!!.uid).addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("Aman", "Listen failed.", e)
                return@addSnapshotListener
            }

            val books = ArrayList<VolumeInfo>()
            for (doc in value!!) {
                doc.toObject(VolumeInfo::class.java)?.let { p ->
                    books.add(p)
                }
            }
            adapter.addAllItems(books)
        }
    }

    private fun setUpRecyclerView() {
        adapter = LibraryAdapter(this)
        binding.recyclerView.layoutManager = GridLayoutManager(activity,2,
            RecyclerView.VERTICAL,false)
        binding.recyclerView.adapter = adapter
    }

    override fun onLibClicked(volumeInfo: VolumeInfo) {
        val fragment = PostFragment()
        val bundle = Bundle()
        bundle.putParcelable("book",volumeInfo)
        fragment.arguments = bundle
        setCurrentFragment(fragment)
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