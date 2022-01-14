package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.adapters.LibraryAdapter
import com.example.bookapp.adapters.OnLibClickListener
import com.example.bookapp.databinding.FragmentReadLaterBinding
import com.example.bookapp.modals.VolumeInfo
import com.example.bookapp.viewModels.BookSharedViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReadLaterFragment : Fragment() , OnLibClickListener {

    lateinit var binding: FragmentReadLaterBinding
    lateinit var adapter: LibraryAdapter
    @Inject
    lateinit var auth : FirebaseAuth
    @Inject lateinit var db : FirebaseFirestore
    private val viewModel: BookSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_read_later,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //val auth = FirebaseAuth.getInstance()
        //val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        val query = bookCollection.whereEqualTo("user.uid",auth.currentUser!!.uid.toString())
            .whereEqualTo("readLater" , true)
        query.addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("Aman", "Listen failed.", e)
                return@addSnapshotListener
            }

            val volumeInfo = ArrayList<VolumeInfo>()
            for (doc in value!!) {
                doc.toObject(VolumeInfo::class.java)?.let { p ->
                    volumeInfo.add(p)
                }
            }
            adapter.addAllItems(volumeInfo)
        }

        adapter = LibraryAdapter(this)
        binding.libraryRecyclerView.layoutManager = GridLayoutManager(activity,2,
            RecyclerView.VERTICAL,false)
        binding.libraryRecyclerView.adapter = adapter
    }


    override fun onLibClicked(volumeInfo: VolumeInfo) {
        viewModel.sendBook(volumeInfo)
        val bottomSheetDialog = BookBottomSheetDialog()
        fragmentManager?.let { bottomSheetDialog.show(it,bottomSheetDialog.tag) }
    }
}