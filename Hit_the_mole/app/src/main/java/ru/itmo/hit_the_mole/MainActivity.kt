package ru.itmo.hit_the_mole

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        val startButton = findViewById<Button>(R.id.startButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val inputText = findViewById<EditText>(R.id.inputText)
        val inputHits = findViewById<EditText>(R.id.inputHits)
        val inputMiss = findViewById<EditText>(R.id.inputMiss)

        startButton.setOnClickListener {
            startGame(startButton, nextButton, inputText, inputHits, inputMiss)
        }

        nextButton.setOnClickListener {
            clickNext(inputText, inputHits, inputMiss)
        }
    }

    private fun startGame(startButton : Button, nextButton: Button, inputText: EditText, inputHits: EditText, inputMiss: EditText) {

        startButton.visibility = View.INVISIBLE
        val onScreen = arrayOf(nextButton, inputText, inputHits, inputMiss)

        for (elem in onScreen) {
            elem.visibility = View.VISIBLE
        }
    }

    private fun clickNext(inputText: EditText, inputHits: EditText, inputMiss: EditText) {
        try {
            val name = inputText.text.toString().trim()
            val hits = inputHits.text.toString().toInt()
            val miss = inputMiss.text.toString().toInt()
            if (name.length !in 3..10) {
                Toast.makeText(applicationContext, "Enter a valid name(3 to 10 symbols)", Toast.LENGTH_SHORT).show()
            } else if (hits !in 1..20) {
                Toast.makeText(applicationContext, "Enter a valid number of hits(1 to 20)", Toast.LENGTH_SHORT).show()
            } else if (miss !in 1..20) {
                Toast.makeText(applicationContext, "Enter a valid number of misses(1 to 20)", Toast.LENGTH_SHORT).show()
            } else {
                beginGame(name, hits, miss)
            }
        } catch (e: java.lang.NumberFormatException) {
            Toast.makeText(applicationContext, "fields must be filled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun beginGame(name: String, hits: Int, miss: Int) {
        val myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE)
        with (myPrefs.edit()) {
            putString("username", name)
            putInt("hits", hits)
            putInt("miss", miss)
            apply()
        }
        val intent = Intent(this@MainActivity, GameActivity::class.java)
        startActivity(intent)
    }

}