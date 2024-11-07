package com.example.user.blogapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.message_activity.*

class MessageActivity : AppCompatActivity() {
    lateinit var shp:SharedPreferences
    lateinit var fauth:FirebaseAuth
    lateinit var fbdb:FirebaseDatabase
    lateinit var userref:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_activity)
        shp=getSharedPreferences("shp",Context.MODE_PRIVATE)
        var username=shp.getString("name","error")
        var userimg=shp.getString("imglink","error")
        fauth= FirebaseAuth.getInstance()

        rv_ma.layoutManager=LinearLayoutManager(this)
        var adapter=UserAdapter(this)
        rv_ma.adapter=adapter

        fbdb= FirebaseDatabase.getInstance()
        userref=fbdb.getReference("user")
        userref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(data: DataSnapshot) {
                var user=ArrayList<User>()
                data.children.forEach {
                    e("def",it.value.toString())
                    var map=it.value as HashMap<String,Any>
                    var u=User()
                    u.name=map["name"].toString()
                    u.userimg=map["userimg"].toString()
                    u.userid=map["userid"].toString()
                    var fa=fauth.uid
                    if(u.userid!=fa) {
                        user.add(u)
                    }
                }
                e("mkkl",user.toString())
                adapter.setUserList(user)
            }

        })


        adapter.setonUserClick(object :MessageDelegate{
            override fun onClick(user: User) {
                var i=Intent(this@MessageActivity,ChatActivity::class.java)
                var bd=Bundle()
                bd.putParcelable("user",user)
                i.putExtras(bd)
                startActivity(i)
            }

        })

    }
}