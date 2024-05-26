package com.example.collegeconnected.data

data class PostData(
    val postId:String,
    val postImg:String,
    val title:String,
    val communityName:String,
    val communityImage:String,
    val postUsername:String,
    val postDescription:String,
    val postLike:Int,
    val postComment:Int
)
