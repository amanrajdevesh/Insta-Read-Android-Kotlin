package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.adapters.LibraryAdapter
import com.example.bookapp.adapters.SearchAdapter
import com.example.bookapp.databinding.FragmentLibraryBinding
import com.example.bookapp.modals.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LibraryFragment : Fragment() {

    lateinit var adapter : LibraryAdapter
    lateinit var binding : FragmentLibraryBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_library,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val bookCollection = db.collection("books")
        val query = bookCollection.whereEqualTo("user.uid" , auth.currentUser!!.uid.toString())
            .whereEqualTo("readLater" , false)
        val option = FirestoreRecyclerOptions.Builder<VolumeInfo>().setQuery(query , VolumeInfo::class.java).build()


        adapter = LibraryAdapter(option)
        binding.libraryRecyclerView.layoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)
        binding.libraryRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}