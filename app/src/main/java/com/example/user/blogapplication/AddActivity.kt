package com.example.user.blogapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.add_activity.*
import kotlinx.android.synthetic.main.register_activity.*
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var fbdb:FirebaseDatabase
    lateinit var postref:DatabaseReference
    lateinit var shp:SharedPreferences
    lateinit var fbst:FirebaseStorage
    lateinit var stref:StorageReference

    lateinit var fbauth:FirebaseAuth
    var postimg:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)
        shp=getSharedPreferences("shp", Context.MODE_PRIVATE)
        fbauth= FirebaseAuth.getInstance()

        fbdb= FirebaseDatabase.getInstance()
        postref=fbdb.getReference("post")
        fbst= FirebaseStorage.getInstance()
        stref=fbst.getReference("image/")
        ib_back_aa.setOnClickListener {
            finish()
        }

        btn_post_aa.setOnClickListener {
            var c= Calendar.getInstance()
            var year=c.get(Calendar.YEAR).toString()
            var month=c.get(Calendar.MONTH).toString()
            var day=c.get(Calendar.DAY_OF_MONTH).toString()
            var date=day+"/"+month+"/"+year

            if (postimg!=null){
                stref.putFile(postimg!!).addOnSuccessListener {
                    stref.downloadUrl.addOnSuccessListener {
                        var post=Post()
                        post.date=date
                        post.name=shp.getString("name","error")
                        post.image=it.toString()
                        post.userimglink=shp.getString("imglink","error")
                        post.title=et_title_aa.text.toString()
                        post.desc=et_desc_aa.text.toString()
                        post.uId=fbauth.uid
                        var postId=postref.push()
                        post.postId=postId.key
                        postId.setValue(post)
                    }
                }.addOnFailureListener{
                    Toast.makeText(this@AddActivity,it.message,Toast.LENGTH_LONG).show()
                }
            }else
            {
                Toast.makeText(this@AddActivity,"Please select the image",Toast.LENGTH_LONG).show()
            }
        }

        iv_post_aa.setOnClickListener {
            var i= Intent(Intent.ACTION_GET_CONTENT)
            i.setType("image/*")
            startActivityForResult(i,123)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==123&&resultCode== Activity.RESULT_OK){
            postimg=data!!.data
            iv_post_aa.setImageURI(postimg)
        }
    }
}