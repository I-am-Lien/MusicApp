package com.li.musicmp3.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.li.musicmp3.ui.play.NotificationCallback
import com.li.musicmp3.ultils.Constant
import com.li.musicmp3.ultils.MediaManager
import com.li.musicmp3.ultils.MediaManager.context
import com.li.musicmp3.R

class MusicService : Service(), MediaPlayer.OnCompletionListener {
    private var mediaSession: MediaSessionCompat? = null
    private var notification: Notification? = null
    private var listener: NotificationCallback? = null

    override fun onCreate() {
        context = this
        mediaSession = MediaSessionCompat(this, Constant.MEDIA_SESSION_TAG)
        MediaManager.mediaPlayer.setOnCompletionListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentFilter = IntentFilter().apply {
            addAction(Constant.ACTION_NEXT)
            addAction(Constant.ACTION_CLOSE)
            addAction(Constant.ACTION_PAUSE)
            addAction(Constant.ACTION_PREVIOUS)
        }
        registerReceiver(musicBroadcast, intentFilter)
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return MyBinder(this)
    }

    override fun onCompletion(p0: MediaPlayer?) {
        changeSong(1)
    }

    fun start() {
        MediaManager.start()
        createNotification(R.drawable.ic_pause)
    }

    fun pause() {
        if (isPlay()) {
            MediaManager.pause()
            createNotification(R.drawable.ic_play)
        } else {
            start()
        }
    }

    fun stop() {
        MediaManager.stop()
    }

    fun isPlay(): Boolean = MediaManager.isPlay()
    fun changeSong(indexSong: Int) {
        MediaManager.changeSong(indexSong)
        createNotification(R.drawable.ic_pause)
    }

    fun create() {
        MediaManager.create(MediaManager.index)
        createNotification(R.drawable.ic_pause)
    }

    fun setCallBack(listener: NotificationCallback) {
        this.listener = listener
    }

    private fun createNotification(playButton: Int) {
        createNotificationChannel()
        notification = context.let {
            NotificationCompat
                .Builder(this, Constant.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_song)
                .setContentTitle(MediaManager.songs[MediaManager.index].name)
                .setContentText(MediaManager.songs[MediaManager.index].artist)
                .addAction(
                    R.drawable.ic_backward,
                    Constant.PRE_TITLE,
                    setUpPendingIntent(Constant.ACTION_PREVIOUS)
                )
                .addAction(
                    playButton,
                    Constant.PLAY_PAUSE_TITLE,
                    setUpPendingIntent(Constant.ACTION_PAUSE)
                )
                .addAction(
                    R.drawable.ic_forward,
                    Constant.NEXT_TITLE,
                    setUpPendingIntent(Constant.ACTION_NEXT)
                )
                .addAction(
                    R.drawable.ic_close,
                    Constant.CLOSE_TITLE,
                    setUpPendingIntent(Constant.ACTION_CLOSE)
                )
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession?.sessionToken)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
        }
        startService(Intent(this, javaClass))
        startForeground(Constant.NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constant.CHANNEL_ID, Constant.CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setUpPendingIntent(action: String): PendingIntent {
        val intent = Intent().setAction(action)
        return PendingIntent.getBroadcast(
            context,
            Constant.REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private val musicBroadcast = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                Constant.ACTION_NEXT -> listener?.onNext()
                Constant.ACTION_PAUSE -> listener?.onNext()
                Constant.ACTION_PREVIOUS -> listener?.onPrevious()
                Constant.ACTION_CLOSE -> {
                    stop()
                    stopSelf()
                    unregisterReceiver(this)
                    stopForeground(true)
                }
            }
        }
    }

    class MyBinder(private var service: MusicService) : Binder() {
        fun getService(): MusicService = service
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MusicService::class.java)
    }

}
