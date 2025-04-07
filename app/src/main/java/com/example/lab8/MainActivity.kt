package com.example.lab8

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var randomCharacterEditText: EditText
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomCharacterEditText = findViewById(R.id.editText_randomCharacter)
        broadcastReceiver = MyBroadcastReceiver()
        serviceIntent = Intent(applicationContext, RandomCharacterService::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button_start -> {
                startService(serviceIntent)
                Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()  // Показать сообщение
            }
            R.id.button_end -> {
                stopService(serviceIntent)
                randomCharacterEditText.setText("")
                Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()  // Показать сообщение
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("my.custom.action.tag.lab6")
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                val data = intent?.getCharExtra("randomCharacter", '?')
                randomCharacterEditText.setText(data.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
