package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.example.bookapp.R
import com.example.bookapp.dao.PostDao
import com.example.bookapp.databinding.FragmentPostBinding
import com.example.bookapp.firebaseModals.Post
import com.squareup.picasso.Picasso

class PostFragment : Fragment() {

    lateinit var binding : FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = PostFragmentArgs.fromBundle(requireArguments())
        val item = args.item
        Picasso.get().load(item.volumeInfo.imageLinks.getImage()).into(binding.imageView)
        binding.etPostTitle.setText(item.volumeInfo.title.toString())
        val author = item.volumeInfo.authors
        binding.etPostAuthor.setText(author?.get(0).toString())
        val type = item.volumeInfo.categories
        if(type.isNotEmpty()){
            binding.etPostType.setText(type[0].toString())
        }
        var readType = ""
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                  readType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.button2.setOnClickListener {
            val body = binding.editText.text.toString()
            val postDao = PostDao()
            val currentTime = System.currentTimeMillis()
            postDao.addPost(
                Post(
                    item.volumeInfo.title,
                    author?.get(0),
                    type[0],
                    item.volumeInfo.imageLinks.getImage(),
                    body,
                    readType,
                    currentTime
                )
            )
        }
    }
}