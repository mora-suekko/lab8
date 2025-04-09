package com.example.lab8

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.*

class RandomCharacterService : Service() {
    private var isRandomGeneratorOn = false
    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    private val TAG = "RandomCharacterService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service Started", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service started...")
        isRandomGeneratorOn = true

        Thread(Runnable {
            try {
                startRandomGenerator()
            } catch (e: Exception) {
                Log.e(TAG, "Error in random generator: ${e.message}", e)
            }
        }).start()

        return START_STICKY
    }

    private fun startRandomGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (isRandomGeneratorOn) {
                    val randomIdx = Random().nextInt(26)
                    val myRandomCharacter = alphabet[randomIdx]
                    Log.i(TAG, "Random Character: $myRandomCharacter")

                    val broadcastIntent = Intent().apply {
                        action = "my.custom.action.tag.lab6"
                        putExtra("randomCharacter", myRandomCharacter)
                    }
                    sendBroadcast(broadcastIntent)
                }
            } catch (e: InterruptedException) {
                Log.i(TAG, "Thread Interrupted.")
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error: ${e.message}", e)
            }
        }
    }

    private fun stopRandomGenerator() {
        isRandomGeneratorOn = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomGenerator()
        Toast.makeText(applicationContext, "Service Stopped", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service Destroyed...")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
