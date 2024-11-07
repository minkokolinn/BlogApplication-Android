package com.example.user.blogapplication

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.conversation_sample.view.*

class ConversationAdapter(var ctxt:Context,var authid:String) : RecyclerView.Adapter<ConversationAdapter.ConversationHolder>() {
    var chat=ArrayList<Chat>()
    fun setNewMessage(chat: ArrayList<Chat>)
    {
        this.chat=chat
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationHolder {
        var v=LayoutInflater.from(ctxt).inflate(R.layout.conversation_sample,parent,false)
        return ConversationHolder(v)
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    override fun onBindViewHolder(holder: ConversationHolder, position: Int) {
        var c=chat.get(position)
        if (c.from==authid){
            holder.itemView.other_ll_conver_sample.visibility=View.GONE
            Glide.with(ctxt).load(c.userimg).into(holder.itemView.civ_my_conver_sample)
            holder.itemView.tv_myabout_conver_sample.text=c.message
        }
        else {
            holder.itemView.my_ll_conver_sample.visibility=View.GONE
            Glide.with(ctxt).load(c.userimg).into(holder.itemView.civ_other_conver_sample)
            holder.itemView.tv_otherabout_conver_sample.text=c.message
        }
    }

    class ConversationHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    }
}