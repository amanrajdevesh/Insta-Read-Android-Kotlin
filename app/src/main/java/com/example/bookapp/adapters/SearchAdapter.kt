package com.example.bookapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.modals.Item
import com.squareup.picasso.Picasso

class SearchAdapter(private val searchAdapterInterface : SearchAdapterInterface) : RecyclerView.Adapter<SearchViewHolder>() {

    private val bookList = ArrayList<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search , parent , false)
        val searchViewHolder = SearchViewHolder(view)
        searchViewHolder.libraryButton.setOnClickListener {
            searchAdapterInterface.onLibraryButtonClicked(bookList[searchViewHolder.adapterPosition])
        }
        searchViewHolder.addLaterButton.setOnClickListener {
            searchAdapterInterface.onAddLaterButtonClicked(bookList[searchViewHolder.adapterPosition])
        }
        searchViewHolder.favButton.setOnClickListener {
            searchAdapterInterface.onFavouriteButtonClicked(bookList[searchViewHolder.adapterPosition])
        }
        searchViewHolder.postButton.setOnClickListener {
            searchAdapterInterface.onPostButtonClicked(bookList[searchViewHolder.adapterPosition])
        }
        return searchViewHolder
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = bookList[position]
        holder.bookName.text = currentItem.volumeInfo.title
        if(currentItem.volumeInfo.authors!=null){
            holder.bookAuthor.text = currentItem.volumeInfo.authors[0]
        }else{
            holder.bookAuthor.text = ""
        }
        val image = currentItem.volumeInfo.imageLinks.getImage()
        if(image.isNotEmpty()){
            Picasso.get().load(image).into(holder.bookImage)}
        //Glide.with(holder.itemView.context).load(currentItem.volumeInfo.imageLinks.image_url).into(holder.bookImage)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun addAllItems(list : List<Item>){
        bookList.clear()
        bookList.addAll(list)
        notifyDataSetChanged()
    }

}

class SearchViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val bookName = view.findViewById<TextView>(R.id.bookName)
    val bookAuthor = view.findViewById<TextView>(R.id.bookAuthor)
    val bookImage = view.findViewById<ImageView>(R.id.bookImage)
    val libraryButton = view.findViewById<ImageView>(R.id.libraryButton)
    val addLaterButton = view.findViewById<ImageView>(R.id.addLaterButton)
    val favButton = view.findViewById<ImageView>(R.id.favButton)
    val postButton = view.findViewById<ImageView>(R.id.postButton)
}

interface SearchAdapterInterface {
    fun onLibraryButtonClicked(item: Item)
    fun onAddLaterButtonClicked(item: Item)
    fun onFavouriteButtonClicked(item : Item)
    fun onPostButtonClicked(item: Item)
}