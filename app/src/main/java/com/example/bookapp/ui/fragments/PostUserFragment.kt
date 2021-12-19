package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.BottomSheetRecyclerAdapter
import com.example.bookapp.adapters.UserViewPagerAdapter
import com.example.bookapp.databinding.FragmentPostUserBinding
import com.example.bookapp.modals.VolumeInfo
import com.example.bookapp.viewModels.BookSharedViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore

class PostUserFragment : Fragment() {

    lateinit var binding : FragmentPostUserBinding
    lateinit var adapter: BottomSheetRecyclerAdapter
    lateinit var viewModel: BookSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_user,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabList = listOf("Post" , "Library" , "Read Later")

        val list = ArrayList<Fragment>()
        list.add(PostFeedFragment())
        list.add(PostLibraryFragment())
        list.add(PostReadLaterFragment())

        binding.viewPager.adapter = UserViewPagerAdapter(childFragmentManager, lifecycle , list)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        setUpRecyclerView()

        viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        viewModel.post.observe(viewLifecycleOwner , Observer {
            binding.apply {
                userName.text = it.user.name
                userPost.text = it.user.post.toString()
                userFav.text = it.user.favourites.toString()
                userLib.text = it.user.library.toString()
            }
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
        val recyclerView = binding.recyclerView2
        recyclerView.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        adapter = BottomSheetRecyclerAdapter()
        recyclerView.adapter = adapter
    }

}