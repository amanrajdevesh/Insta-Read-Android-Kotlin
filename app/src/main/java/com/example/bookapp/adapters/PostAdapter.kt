package com.example.bookapp.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.firebaseModals.Post
import com.example.bookapp.ui.fragments.FeedFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso

class PostAdapter(private val options: FirestoreRecyclerOptions<Post>, val listener: OnPostClickedListener) : FirestoreRecyclerAdapter<Post, PostViewHolder>(options){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        viewHolder.postUserImage.setOnClickListener {
            listener.onPostClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postUser.text = model.user.name
        holder.postRead.text = model.read
        holder.userTitle.text = model.name
        holder.userAuthor.text = model.author
        holder.userThoughts.text = model.body
        if(!TextUtils.isEmpty(model.imageUrl)){
            Picasso.get().load(model.imageUrl).into(holder.userBookImage)
        }
        Picasso.get().load(model.user.imageUrl).into(holder.postUserImage)
        if(TextUtils.isEmpty(model.body)){
            holder.userThoughts.visibility = View.GONE
        }
        if(TextUtils.isEmpty(model.author)&&TextUtils.isEmpty(model.name)&&TextUtils.isEmpty(model.imageUrl)&&TextUtils.isEmpty(model.read)){
            holder.userAuthor.visibility = View.GONE
            holder.userTitle.visibility = View.GONE
            holder.postRead.visibility = View.GONE
            holder.userBookImage.visibility = View.GONE
        }
    }

}

class PostViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val postUserImage = view.findViewById<ImageView>(R.id.postUserImage)
    val postUser = view.findViewById<TextView>(R.id.tvPostUser)
    val postRead = view.findViewById<TextView>(R.id.tvPostRead)
    val userBookImage = view.findViewById<ImageView>(R.id.userBookImage)
    val userTitle = view.findViewById<TextView>(R.id.tvUserTitle)
    val userAuthor = view.findViewById<TextView>(R.id.tvUserAuthor)
    val userThoughts = view.findViewById<TextView>(R.id.userThoughts)
}

interface OnPostClickedListener{
    fun onPostClicked(postId : String)
}