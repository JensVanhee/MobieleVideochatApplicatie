package com.example.mobielevideochatapplicatie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class VideochatActivity : AppCompatActivity() {

    private lateinit var tvName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videochat)
        initData()

        val buttonStopGesprek = findViewById<Button>(R.id.videochatStopGesprekBtn)
        buttonStopGesprek.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initData(){
        tvName = findViewById(R.id.videochatContactName)
        getData()
    }

    private fun getData(){
        var intent = intent.extras
        var name = intent!!.getString("contact")
        tvName.text = name
    }
}