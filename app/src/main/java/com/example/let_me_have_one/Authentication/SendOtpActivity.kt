package com.example.let_me_have_one.Authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.let_me_have_one.Beers.presentation.MainActivity
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.ActivityAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SendOtpActivity : AppCompatActivity() {

    lateinit var binding : ActivityAuthBinding
    lateinit var progressBar : ProgressBar
    lateinit var auth : FirebaseAuth
    var number : String? = null
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()

//        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
//            val firebaseUser = firebaseAuth.currentUser
//            if (firebaseUser != null) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

        if (auth.currentUser != null) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

        binding.buttonGetOtp.setOnClickListener {

            if (binding.inputMobileNumber.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter Mobile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            progressBar = binding.progressBar

            progressBar.visibility = View.VISIBLE

            binding.buttonGetOtp.visibility = View.INVISIBLE

            number = "+91${binding.inputMobileNumber.text.toString()}"

            sendVerificationCode(number!!)



        }


                // Callback function for Phone Auth
                callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    // This method is called when the verification is completed
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                        Log.d("AuthCheck" , "onVerificationCompleted Success")
                    }

                    // Called when verification is failed add log statement to see the exception
                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.d("AuthCheck" , "onVerificationFailed  $e")
                    }

                    // On code is sent by the firebase this method is called
                    // in here we start a new activity where user can enter the OTP
                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        Log.d("AuthCheck","onCodeSent: $verificationId")
                        storedVerificationId = verificationId
                        resendToken = token

                        // Start a new activity using intent
                        // also send the storedVerificationId using intent
                        // we will use this id to send the otp back to firebase
                        val intent = Intent(applicationContext,VerifyActivity::class.java)
                        intent.putExtra("storedVerificationId",storedVerificationId)
                        intent.putExtra("resendToken",resendToken)
                        intent.putExtra("mobile", number)
                        startActivity(intent)
                        finish()
                    }
                }


    }


    // this method sends the verification code
    // and starts the callback of verification
    // which is implemented above in onCreate
    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("AuthStartCheck" , "Auth started")
    }






}