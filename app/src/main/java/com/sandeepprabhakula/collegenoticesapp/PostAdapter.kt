package com.sandeepprabhakula.collegenoticesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sandeepprabhakula.collegenoticesapp.models.Post

class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener:IPostAdapter) :FirestoreRecyclerAdapter<Post,PostAdapter.PostViewHolder>(
    options
){
    class PostViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val comment:ImageView = itemView.findViewById(R.id.comment)
        val commentSection:ConstraintLayout = itemView.findViewById(R.id.commentSection)
        val writeComment:EditText = itemView.findViewById(R.id.writeComment)
        val sendComment:TextView = itemView.findViewById(R.id.sendComment)
        val comments:RecyclerView = itemView.findViewById(R.id.postComments)
        val deletePost:ImageView =itemView.findViewById(R.id.deletePost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val auth = Firebase.auth
        val user = auth.currentUser
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notice_item, parent, false))
        viewHolder.likeButton.setOnClickListener{
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.sendComment.setOnClickListener {
            if(viewHolder.writeComment.text.isNotEmpty()){
                listener.onSendClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id,viewHolder.writeComment.text.toString())
                viewHolder.writeComment.text.clear()
            }else{
                Toast.makeText(it.context,"Empty Comment!!",Toast.LENGTH_SHORT).show()
            }
        }
        viewHolder.deletePost.setOnClickListener {
            listener.onClickDeleted(snapshots.getSnapshot(viewHolder.adapterPosition).id,user?.uid.toString())
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageURL).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.comment.setImageDrawable(ContextCompat.getDrawable(holder.comment.context,R.drawable.ic_outline_mode_comment_24))
        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        var isCommentClicked = false

        // comment click listener
        holder.comment.setOnClickListener {
            if(!isCommentClicked){
                isCommentClicked = true
                holder.commentSection.visibility = View.VISIBLE
            }
            else{
                isCommentClicked = false
                holder.commentSection.visibility = View.GONE
            }
            holder.comments.layoutManager = LinearLayoutManager(it.context)
            val adapter = CommentsAdapter(model.comments)
            holder.comments.adapter = adapter
        }

        // checking if user is valid or not
        if(model.createdBy.uid != currentUserId)holder.deletePost.visibility = View.GONE

        //is post liked or not
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_liked))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unliked))
        }
    }
}
interface IPostAdapter {
    fun onLikeClicked(postId: String)
    fun onSendClicked(postId:String,comment:String)
    fun onClickDeleted(postId:String,uid:String)
}