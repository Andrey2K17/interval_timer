package ru.grinin.intervaltimer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.grinin.intervaltimer.R
import ru.grinin.intervaltimer.entities.TimerUI
import ru.grinin.intervaltimer.screens.training.TrainingProgressBus
import ru.grinin.intervaltimer.screens.training.TrainingUpdate

@AndroidEntryPoint
class TrainingService: LifecycleService() {
    private var isRunning = false
    private lateinit var training: TimerUI
    private var currentIntervalIndex = 0
    private var currentTimeLeft = 0
    private var job: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                training = intent.getParcelableExtra(EXTRA_TRAINING) ?: error("TimerUI is missing in intent!")
                startForeground(NOTIF_ID, createNotification(getString(R.string.training_started)))
                startTraining()
            }
            ACTION_STOP -> {
                stopTraining()
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopTraining() {
        job?.cancel()
        isRunning = false
    }

    private fun startTraining() {
        if (isRunning) return
        isRunning = true

        job = lifecycleScope.launch {
            for ((index, interval) in training.intervals.withIndex()) {
                currentIntervalIndex = index
                currentTimeLeft = interval.time

                beep(1)

                while (currentTimeLeft > 0) {
                    sendUpdate()
                    delay(1000L)
                    currentTimeLeft--
                }
            }
            beep(2)

            sendUpdate(true)
            stopTraining()
            stopSelf()
        }
    }

    private suspend fun sendUpdate(isFinished: Boolean = false) {
        TrainingProgressBus.send(
            TrainingUpdate(
                intervalIndex = currentIntervalIndex,
                timeLeft = currentTimeLeft,
                finished = isFinished
            )
        )
    }

    private suspend fun beep(times: Int) {
        repeat(times) {
            playBeep()
            delay(300)
        }
    }

    private fun playBeep() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
        mediaPlayer.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(content: String): Notification {
        val channelId = createNotificationChannel()
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.interval_training))
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_stat_accessibility)
            .setOngoing(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "training_channel"
        val channelName = "Training Channel"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
        return channelId
    }

    companion object {
        const val ACTION_START = "ACTION_START_TRAINING"
        const val ACTION_STOP = "ACTION_STOP_TRAINING"
        const val ACTION_UPDATE = "ACTION_TRAINING_UPDATE"
        const val EXTRA_TRAINING = "EXTRA_TRAINING"
        const val NOTIF_ID = 1001

        fun start(context: Context, training: TimerUI) {
            val intent = Intent(context, TrainingService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_TRAINING, training)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, TrainingService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}