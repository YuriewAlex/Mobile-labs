package ru.itmo.hit_the_mole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class GameActivity : AppCompatActivity() {

    var score = 0
    var playerScore: TextView? = null
    var hits = 0
    var maxMisses = 0
    var clicked = false
    var misses = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        playerScore = findViewById(R.id.scoreDisplay)

        val displayName = findViewById<TextView>(R.id.Username)

        val myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val name = myPrefs.getString("username", "")
        hits = myPrefs.getInt("hits", 0)
        maxMisses = myPrefs.getInt("miss", 0)
        displayName.text = getString(R.string.player_name, name)

        playGame()
    }

    private fun playGame() {
        if (score >= hits) {
            startActivity(Intent(this@GameActivity, WinActivity::class.java))
        } else if (misses >= maxMisses) {
            startActivity(Intent(this@GameActivity, LoseActivity::class.java))
        } else {
            val image = pickImage()
            clickable(image).start()
        }
    }

    private fun clickable(image: ImageButton): CountDownTimer {
        val playerScore = findViewById<TextView>(R.id.scoreDisplay)
        image.setOnClickListener {
            image.setImageResource(R.drawable.hit)
            clicked = true
            score++
            playerScore.text = getString(R.string.hit_string, score)
        }
        return object : CountDownTimer(1000, 200)  {
            override fun onTick(p0: Long) {
                "do something"
            }

            override fun onFinish() {
                image.setImageResource(R.drawable.hole)
                image.setOnClickListener(null)
                val missDisplay = findViewById<TextView>(R.id.missDisplay)
                if (!clicked) {
                    misses++
                    missDisplay.text = getString(R.string.miss_string, misses)
                }
                clicked = false
                playGame()
            }
        }
    }

    private fun pickImage(): ImageButton {
        val images = findViewById<ConstraintLayout>(R.id.gameLayout).touchables
        images.shuffle()
        val randomImage = images[0] as ImageButton
        randomImage.setImageResource(R.drawable.mole)
        return randomImage
    }
}