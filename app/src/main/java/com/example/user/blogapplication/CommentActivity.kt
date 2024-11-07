package com.example.user.blogapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.e
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.comment_activity.*

class CommentActivity : AppCompatActivity() {
    lateinit var fbdb:FirebaseDatabase
    lateinit var commentref:DatabaseReference
    lateinit var shpre:SharedPreferences
    lateinit var fauth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comment_activity)

        fbdb= FirebaseDatabase.getInstance()
        fauth=FirebaseAuth.getInstance()
        var i=intent
        var bd=i.extras
        var p=Post()
        p= bd.getSerializable("post") as Post

        rv_comment.layoutManager=LinearLayoutManager(this)
        var adapter=CommentAdapter(this,fauth.uid.toString())
        rv_comment.adapter=adapter

        commentref=fbdb.getReference("post").child(p.postId!!).child("comment")
        Glide.with(this).load(p.image).into(iv_comment)
        Glide.with(this).load(p.userimglink).into(civ_profile_comment)
        tv_username_comment.setText(p.name)
        tv_title_comment.setText(p.title)
        tv_desc_comment.setText(p.desc)

        shpre=getSharedPreferences("shp", Context.MODE_PRIVATE)
        var name=shpre.getString("name","error")
        var profileimg=shpre.getString("imglink","error")
        btn_send_comment.setOnClickListener {
            var commentkey=commentref.push()
            var comment=et_comment.text.toString()
            var uId=fauth.uid
            var c=Comment(profileimg,name,comment,commentkey.key,uId)
            commentkey.setValue(c)
        }

        commentref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(data: DataSnapshot) {
                e("123abc",data.value.toString())
                var commentlist=ArrayList<Comment>()
                data.children.forEach {
                    var map=it.value as HashMap<String,Any>
                    e("123abc",map["uid"].toString())
                    var c=Comment(map["img"].toString(),map["name"].toString(),map["comment"].toString(),map["commentKey"].toString(),map["uid"].toString())
                    commentlist.add(c)
                }
                adapter.setNewComment(commentlist)
            }
        })


    }

}
