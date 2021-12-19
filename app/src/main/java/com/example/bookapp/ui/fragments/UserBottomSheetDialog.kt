package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.adapters.FavAdapter
import com.example.bookapp.databinding.FragmentUserBottomSheetDialogBinding
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.modals.VolumeInfo
import com.example.bookapp.viewModels.BookSharedViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import android.R
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.bookapp.adapters.BottomSheetRecyclerAdapter
import com.example.bookapp.adapters.SearchAdapter


class UserBottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var binding : FragmentUserBottomSheetDialogBinding
    lateinit var viewModel: BookSharedViewModel
    lateinit var adapter: BottomSheetRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            com.example.bookapp.R.layout.fragment_user_bottom_sheet_dialog,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        setUpRecyclerView()
        viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        viewModel.post.observe(viewLifecycleOwner, Observer {
            binding.userDialogName.text = it.user.name
            binding.userPost.text = it.user.post.toString()
            binding.userLib.text = it.user.library.toString()
            binding.userFav.text = it.user.favourites.toString()
            Picasso.get().load(it.user.imageUrl).into(binding.userDialogImage)

            val query = bookCollection.whereEqualTo("user.uid",it.user.uid)
                .whereEqualTo("favourite",true)
            query.addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Aman", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val book = ArrayList<VolumeInfo>()
                for (doc in value!!) {
                    doc.toObject(VolumeInfo::class.java)?.let { volume ->
                        book.add(volume)
                        }
                    }
                adapter.addAllItems(book)
                }
            })

        }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView3
        recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        adapter = BottomSheetRecyclerAdapter()
        recyclerView.adapter = adapter
    }

        /*
        val post = viewModel.post.value
        val uid = post?.user?.uid
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        val query = bookCollection.whereEqualTo("user.uid",uid)
            .whereEqualTo("favourite",true)
        val option = FirestoreRecyclerOptions.Builder<VolumeInfo>().setQuery(query, VolumeInfo::class.java).build()

        adapter = FavAdapter(option)
        binding.recyclerView3.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView3.adapter = adapter

         */

}