package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")
    var question=""
    var ansA=""
    var ansB=""
    var ansC=""
    var ansD=""
    var anscorrect=""
    var questionCount=0
    var questionNumber=1
    var user_ans=""
    var user_correct=0
    var user_wrong=0
    lateinit var timer:CountDownTimer
    private val totaltime = 25000L
    var timerContinue=false
    var leftTime = totaltime
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding=ActivityQuizBinding.inflate(layoutInflater)
        var view = quizBinding.root
        setContentView(view)
        game_logic()
        quizBinding.buttonFinish.setOnClickListener {
            sendScore()
        }
        quizBinding.buttonNext.setOnClickListener {
            game_logic()
            resetTimer()
        }
        quizBinding.textViewA.setOnClickListener {
            user_ans="a"
            pauseTimer()
            if(user_ans==anscorrect){
                quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                user_correct++
                quizBinding.textViewCorrect.text=user_correct.toString()
            }else{
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                user_wrong++
                quizBinding.textViewWrong.text=user_wrong.toString()
                findanswer()
            }
            disableClickableOptions()
        }
        quizBinding.textViewB.setOnClickListener {
            user_ans="b"
            pauseTimer()
            if(user_ans==anscorrect){
                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                user_correct++
                quizBinding.textViewCorrect.text=user_correct.toString()
            }else{
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                user_wrong++
                quizBinding.textViewWrong.text=user_wrong.toString()
                findanswer()
            }
            disableClickableOptions()
        }
        quizBinding.textViewC.setOnClickListener {
            user_ans="c"
            pauseTimer()
            if(user_ans==anscorrect){
                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                user_correct++
                quizBinding.textViewCorrect.text=user_correct.toString()
            }else{
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                user_wrong++
                quizBinding.textViewWrong.text=user_wrong.toString()
                findanswer()
            }
            disableClickableOptions()
        }
        quizBinding.textViewD.setOnClickListener {
            user_ans="d"
            pauseTimer()
            if(user_ans==anscorrect){
                quizBinding.textViewD.setBackgroundColor(Color.GREEN)
                user_correct++
                quizBinding.textViewCorrect.text=user_correct.toString()
            }else{
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                user_wrong++
                quizBinding.textViewWrong.text=user_wrong.toString()
                findanswer()
            }
            disableClickableOptions()
        }
    }
    private fun game_logic(){
        restoreOptions()
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount=snapshot.childrenCount.toInt()
                if(questionNumber<=questionCount) {


                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()

                    ansA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    ansB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    ansC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    ansD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    anscorrect = snapshot.child(questionNumber.toString()).child("answer").value.toString()
                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewA.text = ansA
                    quizBinding.textViewB.text = ansB
                    quizBinding.textViewC.text = ansC
                    quizBinding.textViewD.text = ansD

                    quizBinding.progressBarQuiz.visibility=View.INVISIBLE
                    quizBinding.LinearLayoutQuestion.visibility=View.VISIBLE
                    quizBinding.linearLayout.visibility=View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility=View.VISIBLE
                    startTimer()
                }else{
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!!\nYou have answered all the questions.Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow,position->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow,position->
                        val intent = Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()

                }
                questionNumber++
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext,p0.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun findanswer(){
        when(anscorrect){
            "a"->quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b"->quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c"->quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d"->quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }
    fun disableClickableOptions(){
        quizBinding.textViewA.isClickable=false
        quizBinding.textViewB.isClickable=false
        quizBinding.textViewC.isClickable=false
        quizBinding.textViewD.isClickable=false
    }
    fun restoreOptions(){
        quizBinding.textViewA.isClickable=true
        quizBinding.textViewB.isClickable=true
        quizBinding.textViewC.isClickable=true
        quizBinding.textViewD.isClickable=true

        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewD.setBackgroundColor(Color.WHITE)
    }
    private fun startTimer(){
        timer = object:CountDownTimer(leftTime,1000){
            override fun onTick(millisUntilFinished: Long) {
                leftTime=millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickableOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuestion.text="Sorry, time is up continue with next question"
                timerContinue=false
            }

        }.start()
        timerContinue=true

    }
    fun updateCountDownText(){
        val remainingTime:Int = (leftTime/1000).toInt()
        quizBinding.textViewTime.text=remainingTime.toString()
    }
    fun pauseTimer(){
        timer.cancel()
        timerContinue=false

    }
    fun resetTimer(){
        pauseTimer()
        leftTime=totaltime
        updateCountDownText()
    }
    fun sendScore(){
        user?.let {
            val userUID=it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(user_correct)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(user_wrong).addOnSuccessListener {
                Toast.makeText(applicationContext,"Scores sent to database successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity , ResultActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }
}