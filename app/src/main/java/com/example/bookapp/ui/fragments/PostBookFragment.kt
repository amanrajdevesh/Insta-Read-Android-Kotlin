package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.bookapp.R
import com.example.bookapp.dao.PostDao
import com.example.bookapp.databinding.FragmentPostBookBinding
import com.example.bookapp.firebaseModals.Post

class PostBookFragment : Fragment() {

    lateinit var binding : FragmentPostBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_book,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.postButton.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            val post = binding.etTest.text.toString()
            if(TextUtils.isEmpty(post)){
                Toast.makeText(activity,"Nothing written",Toast.LENGTH_SHORT).show()
            }else{
                val postDao = PostDao()
                postDao.addPost(Post(body = post,createdAt = currentTime))
            }
        }
    }
}