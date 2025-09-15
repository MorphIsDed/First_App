package com.example.mynavigationapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout
    private lateinit var btnPower: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var vibrator: Vibrator

    private var currentPage = 0
    private val gradients = arrayOf(
        R.drawable.gradient1,
        R.drawable.gradient2,
        R.drawable.gradient3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        layout = findViewById(R.id.mainLayout)
        btnPower = findViewById(R.id.btnPower)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)

        // Initialize vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Load the first page
        loadPage(currentPage)

        // Button click listeners
        btnPower.setOnClickListener {
            vibratePhone()
            finish() // Close app
        }

        btnPrev.setOnClickListener {
            vibratePhone()
            if (currentPage > 0) {
                currentPage--
                loadPage(currentPage)
            }
        }

        btnNext.setOnClickListener {
            vibratePhone()
            if (currentPage < gradients.size - 1) {
                currentPage++
                loadPage(currentPage)
            }
        }
    }

    // Update the background and button visibility
    private fun loadPage(page: Int) {
        layout.setBackgroundResource(gradients[page])
        btnPrev.visibility = if (page == 0) ImageButton.INVISIBLE else ImageButton.VISIBLE
        btnNext.visibility = if (page == gradients.size - 1) ImageButton.INVISIBLE else ImageButton.VISIBLE
    }

    // Vibrate the phone with compatibility
    private fun vibratePhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
}