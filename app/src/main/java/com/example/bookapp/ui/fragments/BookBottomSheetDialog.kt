package com.example.bookapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentBookBottomSheetDialogBinding
import com.example.bookapp.viewModels.BookSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class BookBottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBookBottomSheetDialogBinding
    lateinit var viewModel: BookSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_book_bottom_sheet_dialog,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BookSharedViewModel::class.java)
        viewModel.volume.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it.imageLinks.getImage()).into(binding.dialogImage)
            binding.dialogTitle.text = it.title
            if(it.authors?.isEmpty() == true)
                binding.dialogAuthor.text = "Unknown"
            else
                binding.dialogAuthor.text = it.authors?.get(0)
            if(it.categories.isEmpty())
                binding.tvCategory.text = "Unknown"
            else
                binding.tvCategory.text = it.categories[0]
            binding.tvPublish.text = it.publisher
            binding.tvLang.text = it.language
            binding.tvCount.text = it.pageCount.toString()

        })
        binding.fullPreview.setOnClickListener {
            //findNavController().navigate(BookBottomSheetDialogDirections.actionBookBottomSheetDialogToBookInfoFragment())
        }
    }
}