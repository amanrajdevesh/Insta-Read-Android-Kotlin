package com.example.bookapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.modals.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso

class LibraryAdapter(private val options: FirestoreRecyclerOptions<VolumeInfo>,
                     private val libClicked : OnLibClickListener) : FirestoreRecyclerAdapter<VolumeInfo, LibraryViewHolder>(options){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val viewHolder =
            LibraryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_book, parent, false))
        viewHolder.image.setOnClickListener {
            libClicked.onLibClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int, model: VolumeInfo) {
        val image = model.imageLinks.getImage()
        if(image.isNotEmpty()){
            Picasso.get().load(image).into(holder.image)}
    }
}

class LibraryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById<ImageView>(R.id.libraryImage)
}

interface OnLibClickListener {
    fun onLibClicked(bookId : String)
}
