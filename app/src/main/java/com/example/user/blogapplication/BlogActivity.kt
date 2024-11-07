package com.example.user.blogapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.blog_activity.*
import java.util.*
import kotlin.collections.ArrayList

class BlogActivity : AppCompatActivity() {
    lateinit var fbdb:FirebaseDatabase
    lateinit var dbref:DatabaseReference
    lateinit var userref:DatabaseReference
    lateinit var shp:SharedPreferences
    lateinit var postref:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_activity)
        fab.setOnClickListener {
            startActivity(Intent(this@BlogActivity,AddActivity::class.java))
        }

        fbdb= FirebaseDatabase.getInstance()
        dbref=fbdb.reference
        userref=fbdb.getReference("user")
        postref=fbdb.getReference("post")

        shp=getSharedPreferences("shp", Context.MODE_PRIVATE)
        var name=shp.getString("name","error")
        var userimglink=shp.getString("imglink","error")

        Glide.with(this).load(userimglink).into(civ_profile_ba)
        tv_name_ba.setText(name)

        rv_ba.layoutManager=LinearLayoutManager(this)
        var adpater=BlogAdpater(this)
        rv_ba.adapter=adpater

        postref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@BlogActivity,"database error",Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                var post=ArrayList<Post>()
                data.children.forEach {
                    var map=it.value as HashMap<String,Any>
                    var pp=Post();
                    pp.uId=map["uId"].toString()
                    pp.title=map["title"].toString()
                    pp.desc=map["desc"].toString()
                    pp.date=map["date"].toString()
                    pp.image=map["image"].toString()
                    pp.postId=map["postId"].toString()
                    pp.name=map["name"].toString()
                    pp.userimglink=map["userimglink"].toString()
                    post.add(pp)
                }
                adpater.setNewList(post)
            }

        })

        adpater.setFunction(object : PostDelegate{
            override fun onPostClick(p: Post) {
                var i=Intent(this@BlogActivity,CommentActivity::class.java)
                var bd=Bundle()
                bd.putSerializable("post",p)
                i.putExtras(bd)
                startActivity(i)
            }

            override fun onDotsClick(p: Post) {

            }

        })

        btn_message_ba.setOnClickListener {
            startActivity(Intent(this@BlogActivity,MessageActivity::class.java))
        }
    }

}