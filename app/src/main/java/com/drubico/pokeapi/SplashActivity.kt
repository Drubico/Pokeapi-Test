package com.drubico.pokeapi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private val  splashTime = 3000
    private val interval = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressBar = findViewById(R.id.progressBar)
        updateProgressBar()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime.toLong())
    }


    private fun updateProgressBar() {
        val handler = Handler(Looper.getMainLooper())
        var progress = 0

        handler.post(object : Runnable {
            override fun run() {
                progress += interval
                progressBar.progress = (progress * 100 / splashTime)
                if (progress < splashTime) {
                    handler.postDelayed(this, interval.toLong())
                }
            }
        })
    }
}