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

class FavAdapter(private val options: FirestoreRecyclerOptions<VolumeInfo>) : FirestoreRecyclerAdapter<VolumeInfo, FavViewHolder>(options){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int, model: VolumeInfo) {
        val image = model.imageLinks.getImage()
        if(image.isNotEmpty()){
            Picasso.get().load(image).into(holder.image)}
    }
}

class FavViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById<ImageView>(R.id.favImage)
}