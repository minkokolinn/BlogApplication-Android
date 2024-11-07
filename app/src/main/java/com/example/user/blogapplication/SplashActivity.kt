package com.example.user.blogapplication

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer
    var i:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        progressbar.setProgress(i)
        countDownTimer = object : CountDownTimer(4000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.v("Log_tag", "Tick of Progress$i$millisUntilFinished")
                i++
                progressbar.progress = i * 100 / (4000 / 1000)

            }

            override fun onFinish() {
                //Do what you want
                i++
                progressbar.progress = 100
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }
        }
        countDownTimer.start()
    }
}