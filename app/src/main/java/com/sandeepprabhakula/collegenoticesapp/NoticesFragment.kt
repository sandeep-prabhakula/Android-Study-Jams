package com.sandeepprabhakula.collegenoticesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.sandeepprabhakula.collegenoticesapp.daos.PostDao
import com.sandeepprabhakula.collegenoticesapp.models.Post

class NoticesFragment : Fragment(),IPostAdapter {
    private lateinit var notices:RecyclerView
    private lateinit var postDao: PostDao
    private lateinit var adapter:PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notices, container, false)
        notices = view.findViewById(R.id.notices)
        setUpRecyclerView()

        FirebaseAuth.getInstance()
        return view
    }
    private fun setUpRecyclerView(){
        postDao = PostDao()
        val query = postDao.postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()
        notices.layoutManager = LinearLayoutManager(activity)
        adapter = PostAdapter(recyclerViewOptions,this)
        notices.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onSendClicked(postId: String, comment: String) {
        postDao.uploadComments(postId,comment)
    }

    override fun onClickDeleted(postId: String,uid:String) {
        postDao.deletePost(postId,uid)
        Toast.makeText(activity,"post deleted",Toast.LENGTH_SHORT).show()
    }
}