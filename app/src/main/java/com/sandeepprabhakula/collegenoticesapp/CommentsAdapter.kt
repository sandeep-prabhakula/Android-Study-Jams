package com.sandeepprabhakula.collegenoticesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeepprabhakula.collegenoticesapp.models.Comments

class CommentsAdapter(private val list:ArrayList<Comments>): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentatorImage:ImageView = itemView.findViewById(R.id.commentatorImage)
        val commentatorId:TextView = itemView.findViewById(R.id.commentatorId)
        val commentatorComment:TextView = itemView.findViewById(R.id.commentatorComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comments_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val model = list[position]
        holder.commentatorId.text = model.commentatorId
        Glide.with(holder.commentatorImage.context).load(model.commentatorImageURL).circleCrop().into(holder.commentatorImage)
        holder.commentatorComment.text = model.commentatorComment
    }


    override fun getItemCount(): Int {
        return list.size
    }
}