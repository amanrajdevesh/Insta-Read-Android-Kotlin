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
import com.example.bookapp.modals.VolumeInfo
import com.squareup.picasso.Picasso

class PostFeedAdapter(val listener: OnPostClickedListener) : RecyclerView.Adapter<PostFeedHolder>() {

    private val postList = ArrayList<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostFeedHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        val holder = PostFeedHolder(view)
        holder.postUserImage.setOnClickListener {
            listener.onPostClicked(postList[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: PostFeedHolder, position: Int) {
            val post = postList[position]
            holder.postUser.text = post.user.name
            holder.postRead.text = post.read
            holder.userTitle.text = post.name
            holder.userAuthor.text = post.author
            holder.userThoughts.text = post.body
            if(!TextUtils.isEmpty(post.imageUrl)){
                Picasso.get().load(post.imageUrl).into(holder.userBookImage)
            }
            Picasso.get().load(post.user.imageUrl).into(holder.postUserImage)
            //holder.postUserImage.visibility = View.GONE
            if(TextUtils.isEmpty(post.body)){
                holder.userThoughts.visibility = View.GONE
            }
            if(TextUtils.isEmpty(post.author)&& TextUtils.isEmpty(post.name)&& TextUtils.isEmpty(post.imageUrl)&& TextUtils.isEmpty(post.read)){
                holder.userAuthor.visibility = View.GONE
                holder.userTitle.visibility = View.GONE
                holder.postRead.visibility = View.GONE
                holder.userBookImage.visibility = View.GONE
            }
        }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun addPost(list : List<Post>){
        postList.clear()
        postList.addAll(list)
        notifyDataSetChanged()
    }

}

class PostFeedHolder(view : View) : RecyclerView.ViewHolder(view){
    val postUserImage = view.findViewById<ImageView>(R.id.postUserImage)
    val postUser  = view.findViewById<TextView>(R.id.tvPostUser)
    val postRead = view.findViewById<TextView>(R.id.tvPostRead)
    val userBookImage = view.findViewById<ImageView>(R.id.userBookImage)
    val userTitle = view.findViewById<TextView>(R.id.tvUserTitle)
    val userAuthor = view.findViewById<TextView>(R.id.tvUserAuthor)
    val userThoughts = view.findViewById<TextView>(R.id.userThoughts)
}

interface OnPostClickedListener{
    fun onPostClicked(post : Post)
}