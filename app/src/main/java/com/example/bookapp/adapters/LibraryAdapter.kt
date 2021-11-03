package com.example.bookapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.R
import com.example.bookapp.modals.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso

class LibraryAdapter(private val options: FirestoreRecyclerOptions<VolumeInfo>) : FirestoreRecyclerAdapter<VolumeInfo, LibraryViewHolder>(options){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_book, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int, model: VolumeInfo) {
        Picasso.get().load(model.imageLinks.getImage()).into(holder.image)
    }
}

class LibraryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val image = view.findViewById<ImageView>(R.id.libraryImage)
}
