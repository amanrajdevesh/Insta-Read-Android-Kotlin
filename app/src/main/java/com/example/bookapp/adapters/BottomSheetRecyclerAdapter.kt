package com.example.bookapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.modals.Item
import com.example.bookapp.modals.VolumeInfo
import com.squareup.picasso.Picasso

class BottomSheetRecyclerAdapter() : RecyclerView.Adapter<BottomViewHolder>() {

    private val bookList = ArrayList<VolumeInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_book, parent, false)
        return BottomViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        val book = bookList[position]
        val image = book.imageLinks.getImage()
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

class BottomViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val image: ImageView = view.findViewById<ImageView>(R.id.libraryImage)

}

