package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgame.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        var view = forgotPasswordBinding.root
        setContentView(view)
        forgotPasswordBinding.buttonReset.setOnClickListener {
            val email = forgotPasswordBinding.editTextForgotEmail.text.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext , "we sent a password reset mail on your email" ,
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext , task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}