package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentPostBinding
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
        binding.etPostType.setText(type[0].toString())
    }
}