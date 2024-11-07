package com.example.user.blogapplication

import android.content.Context
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.sample_view.view.*

class BlogAdpater(var ctxt:Context) : RecyclerView.Adapter<BlogAdpater.BlogHolder>() {
    var post=ArrayList<Post>()
    var delegate:PostDelegate?=null
    fun setFunction(delegate: PostDelegate){
        this.delegate=delegate
    }
    fun setNewList(post:ArrayList<Post>){
        this.post=post
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogHolder {
        var v=LayoutInflater.from(ctxt).inflate(R.layout.sample_view,parent,false)
        return BlogHolder(v)
    }

    override fun getItemCount(): Int {
        return post.size
    }

    override fun onBindViewHolder(holder: BlogHolder, position: Int) {
        var p:Post=post.get(position)
        Glide.with(ctxt).load(p.userimglink).into(holder.itemView.civ_profile_sv)
        holder.itemView.tv_name_sv.setText(p.name)
        holder.itemView.tv_date_sv.setText(p.date)
        Glide.with(ctxt).load(p.image).into(holder.itemView.iv_post_sv)
        holder.itemView.tv_title_sv.setText(p.title)
        holder.itemView.tv_desc_sv.setText(p.desc)
        holder.itemView.setOnClickListener {
            delegate!!.onPostClick(p)
        }
        holder.itemView.ib_menu_sv.setOnClickListener {
            delegate!!.onDotsClick(p)
        }
    }

    class BlogHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    }
}