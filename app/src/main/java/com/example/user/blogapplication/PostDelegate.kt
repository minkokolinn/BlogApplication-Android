package com.example.user.blogapplication

interface PostDelegate {
    fun onPostClick(p: Post)
    fun onDotsClick(p: Post)
}