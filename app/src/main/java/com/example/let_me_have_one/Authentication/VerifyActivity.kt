package com.example.let_me_have_one.Authentication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.let_me_have_one.Beers.presentation.MainActivity
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.ActivityVerifyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {

    lateinit var binding : ActivityVerifyBinding

    lateinit var inputCode1 : EditText
    lateinit var inputCode2 : EditText
    lateinit var inputCode3 : EditText
    lateinit var inputCode4 : EditText
    lateinit var inputCode5 : EditText
    lateinit var inputCode6 : EditText

    lateinit var resendToken: ForceResendingToken
    lateinit var mobileNumber : String

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
     lateinit var auth: FirebaseAuth

    lateinit var otp: java.lang.StringBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        otp = StringBuilder()

        val storedVerificationId = intent.getStringExtra("storedVerificationId")
         resendToken = intent.getParcelableExtra("resendToken")!!
         mobileNumber = intent.getStringExtra("mobile")!!

        Log.d("AuthCheck","Received code ${storedVerificationId}")

        binding = DataBindingUtil.setContentView(this,R.layout.activity_verify)





        binding.textMobile.setText(String.format(
            "+91-%s", intent.getStringExtra("mobile")
        ))

         inputCode1 = binding.inputCode1
         inputCode2 = binding.inputCode2
         inputCode3 = binding.inputCode3
         inputCode4 = binding.inputCode4
         inputCode5 = binding.inputCode5
         inputCode6 = binding.inputCode6

        setupOtpInputsAndProvideOtp()





        binding.buttonVerify.setOnClickListener{
            var otp1 = provideOtp()
            if(otp1.isNotEmpty()){

                Log.d("otp","The merged otp is : ${otp1}")

                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp1)
                signInWithPhoneAuthCredential(credential)


            }else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }

        }

        binding.textResendOtp.setOnClickListener {

            resendVerificationCode(mobileNumber!!,resendToken!!)
            Toast.makeText(this,"Resending OTP!!",Toast.LENGTH_SHORT).show()

        }


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
                token: ForceResendingToken
            ) {
                Log.d("AuthCheck","onCodeSent: $verificationId")
               verificationId
                token

                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase

            }
        }




    }

    private fun provideOtp(): String {

        Log.d("written text is:","${inputCode1.text.toString()}" )
        Log.d("written text is:","${inputCode2.text.toString()}" )
        Log.d("written text is:","${inputCode3.text.toString()}" )
        Log.d("written text is:","${inputCode4.text.toString()}" )
        Log.d("written text is:","${inputCode5.text.toString()}" )
        Log.d("written text is:","${inputCode6.text.toString()}" )



        otp?.append(inputCode1.text.toString())
        otp?.append(inputCode2.text.toString())
        otp?.append(inputCode3.text.toString())
        otp?.append(inputCode4.text.toString())
        otp?.append(inputCode5.text.toString())
        otp?.append(inputCode6.text.toString())

        Log.d("merged text is:","${otp.toString()}")

        return otp.toString()

    }

    private fun  setupOtpInputsAndProvideOtp(){
        inputCode1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){

                    inputCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



        inputCode2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){

                    inputCode3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        inputCode3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){

                    inputCode4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
               // TODO("Not yet implemented")
            }

        })

        inputCode4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              //  TODO("Not yet implemented")

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){

                    inputCode5.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
               // TODO("Not yet implemented")
            }

        })

        inputCode5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              //  TODO("Not yet implemented")

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){

                    inputCode6.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
               // TODO("Not yet implemented")
            }

        })



        inputCode6.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //  TODO("Not yet implemented")


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // TODO("Not yet implemented")
            }

        })

        otp?.append(inputCode1.text)
        otp?.append(inputCode2.text)
        otp?.append(inputCode3.text)
        otp?.append(inputCode4.text)
        otp?.append(inputCode5.text)
        otp?.append(inputCode6.text)

        Log.d("otp","The written otp is: ${otp.toString()}")


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }



}