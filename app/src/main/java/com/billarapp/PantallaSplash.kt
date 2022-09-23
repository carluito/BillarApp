package com.billarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class PantallaSplash :AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        val splash= installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //splash.setKeepOnScreenCondition(true) //no vale pa na

        val intent =Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}