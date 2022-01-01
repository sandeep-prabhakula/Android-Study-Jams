package com.sandeepprabhakula.collegenoticesapp.models

data class Post(
    var text:String = "",
    var createdBy:User = User(),
    var createdAt:Long = 0L,
    var likedBy: ArrayList<String> = ArrayList()
)
