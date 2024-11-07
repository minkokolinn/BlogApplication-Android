package com.example.user.blogapplication

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.e
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.chat_activity.*

class ChatActivity : AppCompatActivity() {
    lateinit var fbdb:FirebaseDatabase
    lateinit var userchatref:DatabaseReference
    lateinit var userotherref:DatabaseReference
    lateinit var fbauth:FirebaseAuth
    lateinit var adapter:ConversationAdapter
    var user=User();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        btn_back_chatact.setOnClickListener {
            finish()
        }
        fbdb= FirebaseDatabase.getInstance()
        userchatref=fbdb.getReference("user")
        fbauth= FirebaseAuth.getInstance()

        rv_chatact.layoutManager=LinearLayoutManager(this)
        adapter=ConversationAdapter(this,fbauth.uid!!)
        rv_chatact.adapter=adapter

        var i=intent
        var bd=i.extras

        user=bd.getParcelable("user")
        Glide.with(this).load(user.userimg).into(civ_profile_chatact)
        tv_name_chatact.text=user.name

        btn_send_chatact.setOnClickListener {
            var message=et_message_chatact.text.toString()
            var from=fbauth.uid
            var shp=this@ChatActivity.getSharedPreferences("shp", Context.MODE_PRIVATE)
            var username=shp.getString("name","error")
            var userimg=shp.getString("imglink","error")

            var ref=userchatref.child(fbauth!!.uid!!).child("chat").child(user.userid!!).push()
            var chat=Chat()
            chat.from=from
            chat.chatkey=ref.key
            chat.message=message
            chat.userimg=userimg
            chat.username=username
            ref.setValue(chat)


            var ref2=userchatref.child(user.userid!!).child("chat").child(fbauth!!.uid!!).push()
            chat.chatkey=ref2.key
            ref2.setValue(chat)

//            setNew()


        }


        setNew()
        userchatref.child(fbauth.uid!!).child("chat").child(user.userid!!).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(data: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(data: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
//        userchatref.child(fbauth.uid!!).child("chat").child(user.userid!!).addListenerForSingleValueEvent(object :)
    }
    fun setNew()
    {
        var q=userchatref.child(fbauth.uid!!).child("chat").child(user.userid!!).orderByValue()
        q.keepSynced(true)
        q.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(data: DataSnapshot) {
                e("conversation",data.value.toString())
                var chatlist=ArrayList<Chat>()
                data.children.forEach {
                    e("conversation",it.value.toString())
                    var map=it.value as HashMap<String,Any>
                    var c=Chat()
                    c.from=map["from"].toString()
                    c.userimg=map["userimg"].toString()
                    c.chatkey=map["chatkey"].toString()
                    c.message=map["message"].toString()
                    c.username=map["username"].toString()
                    chatlist.add(c)
                }
                adapter.setNewMessage(chatlist)
            }

        })
    }
}