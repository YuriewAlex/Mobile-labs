package ru.itmo.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

private var userName: String? = null
private var SelectedTopicName: String? = null

class ChooseActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_test)

        userName = intent.getStringExtra(Constants.USER_NAME)

        val kotlin = findViewById<LinearLayout>(R.id.kotlinLayout)
        val android = findViewById<LinearLayout>(R.id.androidLayout)

        val btn = findViewById<Button>(R.id.start_quiz_btn)


        kotlin.setOnClickListener {
            SelectedTopicName = "Kotlin"
            kotlin.setBackgroundResource(R.drawable.round_back_white_stroke)
            android.setBackgroundResource(R.drawable.round_back_whtie)
        }
        android.setOnClickListener {
            SelectedTopicName = "Android"
            android.setBackgroundResource(R.drawable.round_back_white_stroke)

            kotlin.setBackgroundResource(R.drawable.round_back_whtie)
        }
        btn.setOnClickListener {
            if (SelectedTopicName == null) {
                Toast.makeText(this, "Select topic", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ChooseActivity, QuestionActivity::class.java)
                intent.putExtra(Constants.USER_NAME, userName)
                intent.putExtra("topic", SelectedTopicName)
                startActivity(intent)
                finish()
            }
        }
    }
}