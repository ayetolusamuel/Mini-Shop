package com.codingwithset.minie_commerce.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.codingwithset.minie_commerce.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        //delay [SplashActivity] for 2sec
        Handler().postDelayed({
            startActivity(Intent(applicationContext, ProductActivity::class.java))
            finish()
            //add fade transition when navigating from [SplashActivity] to [ProductActivity]
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }, 2000)
    }
}
