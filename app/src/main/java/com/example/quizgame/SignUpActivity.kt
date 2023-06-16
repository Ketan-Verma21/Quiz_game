package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpBinding: ActivitySignUpBinding
    val auth: FirebaseAuth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding=ActivitySignUpBinding.inflate(layoutInflater)
        var view = signUpBinding.root
        setContentView(view)
        signUpBinding.buttonSignUp.setOnClickListener {
            val email = signUpBinding.editTextSignUpEmail.text.toString()
            val password=signUpBinding.editTextSignUpPassword.text.toString()
            signUpWithFirebase(email,password)
        }
    }
    fun signUpWithFirebase(Email:String , Password:String){
        signUpBinding.progressBarSignUp.visibility= View.VISIBLE
        signUpBinding.buttonSignUp.isClickable=false
        auth.createUserWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"Your account has been created",Toast.LENGTH_SHORT).show()
                    finish()
                    signUpBinding.progressBarSignUp.visibility= View.INVISIBLE
                    signUpBinding.buttonSignUp.isClickable=true
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}