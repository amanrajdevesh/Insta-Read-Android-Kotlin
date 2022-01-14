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

class LibraryAdapter(private val libClicked : OnLibClickListener) : RecyclerView.Adapter<LibraryViewHolder>(){

    private val bookList = ArrayList<VolumeInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val viewHolder = LibraryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_book, parent, false))
        viewHolder.image.setOnClickListener {
            libClicked.onLibClicked(bookList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val volume = bookList[position]
        val image = volume.imageLinks.getImage()
        if(image.isNotEmpty()){
            Picasso.get().load(image).into(holder.image)}
     }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun addAllItems(list : List<VolumeInfo>){
        bookList.clear()
        bookList.addAll(list)
        notifyDataSetChanged()
    }

}

class LibraryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById<ImageView>(R.id.libraryImage)
}

interface OnLibClickListener {
    fun onLibClicked(volumeInfo: VolumeInfo)
}
