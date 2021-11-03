package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.adapters.LibraryAdapter
import com.example.bookapp.adapters.UserViewPagerAdapter
import com.example.bookapp.dao.UserDao
import com.example.bookapp.databinding.FragmentSearchBinding
import com.example.bookapp.databinding.FragmentUserBinding
import com.example.bookapp.firebaseModals.User
import com.example.bookapp.modals.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserFragment : Fragment() {

    lateinit var binding: FragmentUserBinding
    lateinit var adapter: LibraryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabList = listOf("Post" , "Library" , "Read Later")

        val list = ArrayList<Fragment>()
        list.add(UserPostFragment())
        list.add(LibraryFragment())
        list.add(ReadLaterFragment())

        binding.viewPager.adapter = UserViewPagerAdapter(childFragmentManager, lifecycle , list)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        val query = bookCollection.whereEqualTo("user.uid",auth.currentUser!!.uid.toString())
            .whereEqualTo("favourite",true)
        val option = FirestoreRecyclerOptions.Builder<VolumeInfo>().setQuery(query,VolumeInfo::class.java).build()

        adapter = LibraryAdapter(option)
        binding.recyclerView2.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView2.adapter = adapter

        val userDao = UserDao()
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUser(auth.currentUser!!.uid).await().toObject(User::class.java)
            withContext(Dispatchers.Main){
                Picasso.get().load(user?.imageUrl).into(binding.userImage)
                binding.userName.text = user?.name
                binding.userPost.text = user?.post.toString()
                binding.userFav.text = user?.favourites.toString()
                binding.userLib.text = user?.library.toString()
            }
        }
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