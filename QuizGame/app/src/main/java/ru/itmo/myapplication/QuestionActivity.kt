package ru.itmo.myapplication


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_question.*
import java.util.Timer
import java.util.TimerTask

class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var quizTimer: Timer? = null

    private var totalTimeInMins = 1
    private var seconds = 30

    private var currentPosition: Int = 1
    private var questionsList: ArrayList<Questions>? = null
    private var selectedOptionPosition: Int = 0
    private var correctAnswers: Int = 0
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val selectedTopicName = intent.getStringExtra("topic")
        topic_name.setText(intent.getStringExtra("topic"))
        userName = intent.getStringExtra(Constants.USER_NAME)
        statTimer()

        questionsList = selectedTopicName?.let { Constants.getQuestions(it) }

        setQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        sub_btn.setOnClickListener(this)
        back_button.setOnClickListener(this)
    }

    private fun statTimer() {
        val timer = findViewById<TextView>(R.id.timer)
        quizTimer = Timer()

        quizTimer!!.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                if ((seconds == 0) && (totalTimeInMins != 0)) {
                    totalTimeInMins--
                    seconds = 59
                } else if (totalTimeInMins == 0 && seconds == 0) {
                    quizTimer!!.purge()
                    quizTimer!!.cancel()

                    Toast.makeText(this@QuestionActivity, "Time over", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@QuestionActivity, ResultActivity::class.java)
                    intent.putExtra(Constants.USER_NAME, userName)
                    intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers)
                    intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList!!.size)
                    startActivity(intent)
                    finish()
                } else {
                    seconds--
                }
                runOnUiThread(object : Runnable {
                    override fun run() {
                        var finalMinutes = totalTimeInMins.toString()
                        var finalSeconds = seconds.toString()
                        if (finalMinutes.length == 1) {
                            finalMinutes = "0"+finalMinutes
                        }

                        if (finalSeconds.length == 1) {
                            finalSeconds = "0"+finalSeconds
                        }
                        timer.setText(finalMinutes + ":" + finalSeconds)
                    }
                })
            }
        }, 1000, 1000)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_option_three,3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_option_four, 4)
            }
            R.id.sub_btn -> {
                if(selectedOptionPosition == 0) {
                    currentPosition++

                    when {
                        currentPosition <= questionsList!!.size -> {
                            setQuestion()
                        } else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, userName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = questionsList?.get(currentPosition-1)
                    if (question!!.correctAnswer != selectedOptionPosition) {
                        answerView(selectedOptionPosition, R.drawable.wrong_option)
                    } else {
                        correctAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option)

                    if(currentPosition == questionsList!!.size) {
                        sub_btn.text = "ГОТОВО"
                    } else {
                        sub_btn.text = "СЛЕДУЮЩИЙ ВОПРОС"
                    }
                    selectedOptionPosition = 0
                }
            }
            R.id.back_button -> {
                if (currentPosition > 1) {
                    currentPosition--
                    setQuestion()
                } else {
                    quizTimer!!.purge()
                    quizTimer!!.cancel()

                    startActivity(Intent(this@QuestionActivity, ChooseActivity::class.java))
                    finish()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {

        val question = questionsList!![currentPosition - 1]

        defaultOptionsView()

        if (currentPosition == questionsList!!.size) {
            sub_btn.text = "ГОТОВО"
        } else {
            sub_btn.text = "ОТПРАВИТЬ"
        }
        progressBar.max = questionsList!!.size
        progressBar.progress = currentPosition
        tv_progress.text = "$currentPosition" + "/" + progressBar.max

        tv_question.text = question.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        //#7A8089

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.border_tv
            )
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        defaultOptionsView()
        selectedOptionPosition = selectedOptionNumber

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option
        )
    }
}