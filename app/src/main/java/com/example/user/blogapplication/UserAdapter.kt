package com.example.user.blogapplication

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_sample.view.*

class UserAdapter(var ctxt:Context) : RecyclerView.Adapter<UserAdapter.UserHolder>() {
    var user=ArrayList<User>()
    fun setUserList(user:ArrayList<User>){
        this.user=user
        notifyDataSetChanged()
    }
    var delegate:MessageDelegate?=null
    fun setonUserClick(delegate: MessageDelegate)
    {
        this.delegate=delegate
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        var v=LayoutInflater.from(ctxt).inflate(R.layout.user_sample,parent,false)
        return UserHolder(v)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        var u=user.get(position)
        Glide.with(ctxt).load(u.userimg).into(holder.itemView.civ_userprofile_us)
        holder.itemView.tv_username_us.text=u.name
        holder.itemView.setOnClickListener {
            delegate!!.onClick(u)
        }
    }

    class UserHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    }
}