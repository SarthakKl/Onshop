package com.example.shoppingapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.shoppingapp.Activities.TheMainActivity.TheMainActivity
import com.example.shoppingapp.R
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser

import androidx.annotation.NonNull
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*Remove below comment if you want to have a full screen

         */
        //@Suppress("DEPRECATION")
        //window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val window: Window = this@SplashScreen.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Splash_Screeen_Statusbar_color)


        Handler(Looper.getMainLooper()).postDelayed({

                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val intent = Intent(this, TheMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finish()
                }

        },1500)
    }
}