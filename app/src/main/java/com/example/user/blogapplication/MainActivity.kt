package com.example.user.blogapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log.e
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var fbdb:FirebaseDatabase
    lateinit var userref:DatabaseReference
    lateinit var fbauth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_register_am.setOnClickListener {
            startActivity(Intent(this@MainActivity,RegisterActivity::class.java))
        }
        fbdb= FirebaseDatabase.getInstance()
        userref=fbdb.getReference("user")
        fbauth= FirebaseAuth.getInstance()
        btn_login_am.setOnClickListener {
            fbauth.signInWithEmailAndPassword(
                    et_email_am.text.toString(),et_pass_am.text.toString()
            ).addOnSuccessListener {
                saveShare(it)
                var ab:AlertDialog.Builder=AlertDialog.Builder(this)
                ab.setTitle("Login Success")
                ab.setMessage("Congratulation!!!\nYou can use this application")
                ab.setPositiveButton("OK",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.dismiss()
                        startActivity(Intent(this@MainActivity,BlogActivity::class.java))
                        finish()

                    }
                })
                ab.show()
            }.addOnFailureListener {
                var ab:AlertDialog.Builder=AlertDialog.Builder(this)
                ab.setTitle("Login Fail")
                ab.setMessage("Your Name and Password are wrong!!\nPlease register first")
                ab.setPositiveButton("OK",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.dismiss()
                    }
                })
                ab.show()
            }
        }
    }

    private fun saveShare(it: AuthResult?) {
        userref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(data: DataSnapshot) {
//                data.children.forEach {
//                    e("123",it.value.toString())
//                }
                e("123",data.child(it!!.user.uid).value.toString())

                var map=data.child(it!!.user.uid).value as HashMap<String,Any>
                var shp=this@MainActivity.getSharedPreferences("shp", Context.MODE_PRIVATE)
                shp.edit().putString("name",map["name"].toString()).apply()
                shp.edit().putString("imglink",map["userimg"].toString()).apply()

            }

        })
    }
}
