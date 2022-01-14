package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.adapters.LibraryAdapter
import com.example.bookapp.adapters.OnLibClickListener
import com.example.bookapp.dao.BookDao
import com.example.bookapp.databinding.FragmentLibraryBinding
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.modals.VolumeInfo
import com.example.bookapp.viewModels.BookSharedViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : Fragment() , OnLibClickListener {

    private lateinit var adapter : LibraryAdapter
    lateinit var binding : FragmentLibraryBinding
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var bookDao: BookDao
    @Inject lateinit var db : FirebaseFirestore
    private val viewModel: BookSharedViewModel by activityViewModels()
    //lateinit var viewModel: BookSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_library,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //auth = FirebaseAuth.getInstance()
        //val db = FirebaseFirestore.getInstance()
        //bookDao = BookDao()
        //viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)

        val bookCollection = db.collection("books")
        val query = bookCollection.whereEqualTo("user.uid" , auth.currentUser!!.uid)
            .whereEqualTo("readLater" , false)
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
        //val option = FirestoreRecyclerOptions.Builder<VolumeInfo>().setQuery(query , VolumeInfo::class.java).build()

        adapter = LibraryAdapter(this)
        binding.libraryRecyclerView.layoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)
        binding.libraryRecyclerView.adapter = adapter
    }


    override fun onLibClicked(volumeInfo: VolumeInfo) {
        viewModel.sendBook(volumeInfo)
        val bottomSheetDialog = BookBottomSheetDialog()
        fragmentManager?.let { bottomSheetDialog.show(it,bottomSheetDialog.tag) }
    }

    /*
    private fun showDialog(book : VolumeInfo) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog);

        val bookTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
        bookTitle.text = book.title

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

     */

}