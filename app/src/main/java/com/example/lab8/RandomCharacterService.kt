package com.example.lab8

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*

class RandomCharacterService : Service() {
    private var isRandomGeneratorOn = false
    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    private val TAG = "RandomCharacterService"
    private val CHANNEL_ID = "RandomCharChannel"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Сервис создан")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand вызван")

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Генерация символов")
            .setContentText("Сервис работает...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()

        startForeground(1, notification)

        isRandomGeneratorOn = true

        Thread {
            try {
                startRandomGenerator()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка генерации: ${e.message}", e)
            }
        }.start()

        return START_STICKY
    }

    private fun startRandomGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000)
                val randomIdx = Random().nextInt(alphabet.size)
                val myRandomCharacter = alphabet[randomIdx]
                Log.i(TAG, "Случайный символ: $myRandomCharacter")

                val broadcastIntent = Intent().apply {
                    action = "my.custom.action.tag.lab6"
                    putExtra("randomCharacter", myRandomCharacter)
                }
                sendBroadcast(broadcastIntent)
            } catch (e: InterruptedException) {
                Log.i(TAG, "Поток прерван.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRandomGeneratorOn = false
        Log.i(TAG, "Сервис остановлен")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Random Character Generator",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
