package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.R
import com.example.bookapp.adapters.FavAdapter
import com.example.bookapp.adapters.FavouriteAdapter
import com.example.bookapp.adapters.UserViewPagerAdapter
import com.example.bookapp.dao.UserDao
import com.example.bookapp.databinding.FragmentUserBinding
import com.example.bookapp.firebaseModals.User
import com.example.bookapp.modals.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UserFragment : Fragment() {

    lateinit var binding: FragmentUserBinding
    lateinit var adapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabList = listOf("Post" , "Library" , "Read Later")

        val list = ArrayList<Fragment>()
        list.add(UserPostFragment())
        list.add(LibraryFragment())
        list.add(ReadLaterFragment())
        //list.add(BookDialogFragment())

        binding.viewPager.adapter = UserViewPagerAdapter(childFragmentManager, lifecycle , list)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("books")
        val query = bookCollection.whereEqualTo("user.uid",auth.currentUser!!.uid)
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
            adapter.addAllFav(book)
        }

        adapter = FavouriteAdapter()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit ->{
                val fragment = EditUserFragment()
                setCurrentFragment(fragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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