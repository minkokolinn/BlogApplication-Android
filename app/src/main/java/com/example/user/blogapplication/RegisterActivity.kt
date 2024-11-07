package com.example.user.blogapplication

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.register_activity.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var fbauth:FirebaseAuth
    lateinit var fbdb:FirebaseDatabase
    lateinit var dbref:DatabaseReference
    lateinit var userref:DatabaseReference

    lateinit var fbst: FirebaseStorage
    lateinit var strf: StorageReference
    var userimglink:Uri?=null

    lateinit var pd:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        fbauth= FirebaseAuth.getInstance()

        pd=ProgressDialog(this)
        pd.setMessage("Loading.....")

        fbst= FirebaseStorage.getInstance()
        strf=fbst.reference
        ib_back_ra.setOnClickListener {
            finish()
        }
        fbdb= FirebaseDatabase.getInstance()
        dbref=fbdb.reference
        userref=fbdb.getReference("user")

        civ_ra.setOnClickListener {
            var i= Intent(Intent.ACTION_GET_CONTENT)
            i.setType("image/*")
            startActivityForResult(i,123)
        }

        btn_reg_ra.setOnClickListener { a ->
            if (userimglink==null){
                Toast.makeText(this@RegisterActivity,"Please select your profile",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //
            var name=et_name_ra.text.toString()
            var email=et_email_ra.text.toString()
            var pass=et_pass_ra.text.toString()
            var cpass=et_cpass_ra.text.toString()
            if (name.isEmpty()){
                et_name_ra.error="Name is required"
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                et_pass_ra.error="Password is required"
                return@setOnClickListener
            }
            if (cpass.isEmpty()){
                et_cpass_ra.error="Confirm password is required"
                return@setOnClickListener
            }
            if (pass!=cpass){
                et_cpass_ra.error="Password is not same"
                return@setOnClickListener
            }
            if (email.isEmpty()){
                et_email_ra.error="Email is required"
                return@setOnClickListener
            }
            pd.show()
            fbauth.createUserWithEmailAndPassword(
                    et_email_ra.text.toString(),et_pass_ra.text.toString()
            ).addOnSuccessListener {
                Toast.makeText(this@RegisterActivity,it.user.email,Toast.LENGTH_LONG).show()
                registerProfile(it,name)

            }.addOnFailureListener {
                Toast.makeText(this@RegisterActivity,it.message,Toast.LENGTH_LONG).show()
                if (pd.isShowing){
                    pd.dismiss()
                }
            }
        }

    }

    private fun registerProfile(ar: AuthResult?,name:String) {
        var millisec = Calendar.getInstance().timeInMillis
        strf = fbst.reference.child("image" + millisec + "/")
        if (userimglink!=null){
            strf.putFile(userimglink!!).addOnSuccessListener {
                Toast.makeText(this@RegisterActivity, "Success", Toast.LENGTH_LONG).show()
                strf.downloadUrl.addOnSuccessListener {bb->
                    Toast.makeText(this@RegisterActivity, bb.path.toString(), Toast.LENGTH_LONG).show()
                    var user=User()
                    user.name=name
                    user.userimg=bb.toString()
                    user.userid=fbauth.uid
                    userref.child(ar!!.user.uid).setValue(user)
                    if (pd.isShowing){
                        pd.dismiss()
                        finish()
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==123&&resultCode== Activity.RESULT_OK){
            userimglink=data!!.data
            civ_ra.setImageURI(userimglink)
        }
    }
}