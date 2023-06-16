package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.quizgame.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var googleSignInClient:GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    val auth:FirebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding= ActivityLoginBinding.inflate(layoutInflater)
        var view = loginBinding.root
        setContentView(view)
        val textofgooglebutton = loginBinding.buttonGoggleSignIn.getChildAt(0) as TextView
        textofgooglebutton.text="Continue With Google"
        textofgooglebutton.setTextColor(Color.BLACK)
        textofgooglebutton.textSize=18F
        registerActivityForGoogleSignIn()
        loginBinding.buttonSignIn.setOnClickListener {
            val userEmail  = loginBinding.editTextLoginEmail.text.toString()
            val userPassword = loginBinding.editTextLoginPassword.text.toString()
            signinwithfirebase(userEmail,userPassword)
        }
        loginBinding.buttonGoggleSignIn.setOnClickListener {
            signingoogle()
        }
        loginBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity , SignUpActivity::class.java)
            startActivity(intent)

        }
        loginBinding.textViewIforgotMyPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    fun signinwithfirebase(userEmail : String , userPassword : String){
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(applicationContext , "Welcome to Quiz Game"  , Toast.LENGTH_SHORT).show()
                    val intent  = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext , task.exception?.toString()  , Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onStart() {
        super.onStart()
        val user  = auth.currentUser
        if(user != null){
            Toast.makeText(applicationContext , "Welcome to Quiz Game"  , Toast.LENGTH_SHORT).show()
            val intent  = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun signingoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1093297838504-tt38funjtb5v3s6uamm5askmg4tn6foq.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        signin()

    }
    private fun signin(){
        val signInIntent :Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }
    private fun registerActivityForGoogleSignIn(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->
                val resultcode=result.resultCode
                val data=result.data
                if(resultcode== RESULT_OK && data!=null){
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebasesigninwithgoogle(task)
                }

            })
    }
    private fun firebasesigninwithgoogle(task: Task<GoogleSignInAccount>){
        try{
            val account :GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext , "Welcome to Quiz Game"  , Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)

        }catch(e:ApiException){
            Toast.makeText(applicationContext , e.localizedMessage , Toast.LENGTH_SHORT).show()
        }


    }
    private fun firebaseGoogleAccount(account :GoogleSignInAccount){
        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener {task->
            if(task.isSuccessful){
//                val user = auth.currentUser

            }else{

            }

        }
    }
}
