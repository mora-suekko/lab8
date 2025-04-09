package com.example.lab8

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var characterTextView: TextView
    private lateinit var serviceIntent: Intent

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val data = intent?.getCharExtra("randomCharacter", '?')
            characterTextView.text = data.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        characterTextView = findViewById(R.id.characterTextView)
        serviceIntent = Intent(applicationContext, RandomCharacterService::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button_start -> {
                startService(serviceIntent)
                Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
            }
            R.id.button_end -> {
                stopService(serviceIntent)
                characterTextView.text = ""
                Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("my.custom.action.tag.lab6")
        registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}
