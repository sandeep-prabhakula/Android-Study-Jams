package com.sandeepprabhakula.collegenoticesapp.models

data class Comments(
    var commentatorId:String = "",
    var commentatorComment:String = "",
    var commentatorImageURL:String = "",
    var commentPostedAt:Long = 0L
)
