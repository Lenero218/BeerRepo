package com.example.let_me_have_one

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.let_me_have_one.Authentication.SendOtpActivity
import com.example.let_me_have_one.Beers.presentation.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.lang.Thread.sleep

class SplashActivity : AppCompatActivity() {


    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putString("token","1")
        editor.apply()



        Handler(Looper.getMainLooper()).postDelayed(
            {

                // This method will be executed once the timer is over

                if(auth.currentUser != null){
                    startActivity(Intent(this,MainActivity::class.java))

              }else{
                    val intent = Intent(this,SendOtpActivity::class.java)
                    startActivity(intent)

              }
                finish()


            },
            3000 // value in milliseconds
        )




    }
}