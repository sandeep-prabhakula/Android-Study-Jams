package com.sandeepprabhakula.collegenoticesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.sandeepprabhakula.collegenoticesapp.daos.PostDao

class NewNoticeFragment : Fragment() {
    private lateinit var postDao: PostDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_new_notice, container, false)
        val noticeBoard:EditText = view.findViewById(R.id.noticeBoard)
        val post: Button = view.findViewById(R.id.addNewNotice)
        postDao = PostDao()
        post.setOnClickListener {
            val noticeText = noticeBoard.text.toString()
            if(noticeText.isNotEmpty()){
                postDao.addPost(noticeText)
            }
            findNavController().navigate(R.id.action_newNoticeFragment_to_noticesFragment)
        }
        return view
    }
}