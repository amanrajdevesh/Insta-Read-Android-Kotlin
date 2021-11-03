package com.example.bookapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentFeedBinding
import com.example.bookapp.firebaseModals.Name
import com.google.firebase.firestore.FirebaseFirestore


class FeedFragment : Fragment() {

    /*
    companion object{
        private const val IMAGE_URL = "https://books.google.com/books/content?id=I3ymoAEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        private const val IMAGE_URL_2 = "https://images.mktw.net/im-359250/social"
    }
    */
    lateinit var binding : FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_feed , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = FirebaseFirestore.getInstance()
        val demoCollection = db.collection("demo")
        binding.button.setOnClickListener {
            val name = binding.etTest.toString()
            demoCollection.document().set(Name(name))
        }
    }
}