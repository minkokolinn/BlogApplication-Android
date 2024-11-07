package com.example.user.blogapplication

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.comment_sample.view.*

class CommentAdapter(var ctxt:Context,var uid:String) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    var comment=ArrayList<Comment>()
    fun setNewComment(comment: ArrayList<Comment>){
        this.comment=comment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        var v=LayoutInflater.from(ctxt).inflate(R.layout.comment_sample,parent,false)
        return CommentHolder(v)
    }

    override fun getItemCount(): Int {
        return comment.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        var c:Comment=comment.get(position)
        e("abc",c.uId+"/"+uid)
        if(c.uId==uid){
            holder.itemView.other_ll.visibility=View.GONE
            Glide.with(ctxt).load(c.img).into(holder.itemView.civ_me_cs)
            holder.itemView.tv_myname_cs.text=c.name
            holder.itemView.tv_myabout_cs.text=c.comment
        }else{
            holder.itemView.me_ll.visibility=View.GONE
            Glide.with(ctxt).load(c.img).into(holder.itemView.civ_other_cs)
            holder.itemView.tv_othername_cs.text=c.name
            holder.itemView.tv_otherabout_cs.text=c.comment
        }

    }

    class CommentHolder(itemView:View): RecyclerView.ViewHolder(itemView)
}