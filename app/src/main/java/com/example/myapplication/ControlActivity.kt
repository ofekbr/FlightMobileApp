package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        val URL = intent.getStringExtra("EXTRA_TEXT")
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = URL
    }
}