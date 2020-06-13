package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText

class ConnectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun connect(view: View) {
        val URLText = findViewById<EditText>(R.id.URLText)
        val URL = URLText.text.toString()
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("EXTRA_TEXT", URL)
        startActivity(intent)
    }
}