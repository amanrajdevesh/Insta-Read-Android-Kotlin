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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.adapters.BottomSheetRecyclerAdapter
import com.example.bookapp.databinding.FragmentPostReadLaterBinding
import com.example.bookapp.modals.VolumeInfo
import com.example.bookapp.viewModels.BookSharedViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostReadLaterFragment : Fragment() {

    lateinit var binding : FragmentPostReadLaterBinding
    lateinit var adapter: BottomSheetRecyclerAdapter
    //lateinit var viewModel: BookSharedViewModel
    private val viewModel: BookSharedViewModel by activityViewModels()
    @Inject
    lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_read_later,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        //val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        //viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        viewModel.post.observe(viewLifecycleOwner, Observer {
            val uid = it.user.uid
            val query = bookCollection.whereEqualTo("user.uid" , uid)
                .whereEqualTo("readLater" , true)
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
        adapter = BottomSheetRecyclerAdapter()
        binding.libraryRecyclerView.layoutManager = GridLayoutManager(activity,2,
            RecyclerView.VERTICAL,false)
        binding.libraryRecyclerView.adapter = adapter
    }

}