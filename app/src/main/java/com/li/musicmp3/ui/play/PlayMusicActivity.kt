package com.li.musicmp3.ui.play

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import com.li.musicmp3.R
import com.li.musicmp3.service.MusicService
import com.li.musicmp3.ultils.Constant
import com.li.musicmp3.ultils.MediaManager
import com.li.musicmp3.ultils.MediaUltils
import com.li.musicmp3.ultils.multiViewClick
import kotlinx.android.synthetic.main.activity_play_music.*

class PlayMusicActivity : AppCompatActivity(),
    View.OnClickListener,
    ServiceConnection,
    NotificationCallback {

    private lateinit var service: MusicService
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        multiViewClick(imageNext, imagePrevious, imagePlay)
        createUIThread()
        bindService(MusicService.getIntent(this), this, Context.BIND_AUTO_CREATE)
        updateSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(this)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        service.stop()
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder: MusicService.MyBinder = p1 as MusicService.MyBinder
        service = binder.getService()
        service.setCallBack(this)
        service.create()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imageNext -> onNext()
            R.id.imagePrevious -> onPrevious()
            R.id.imagePlay -> onPlayPause()
        }
    }

    override fun onNext() {
        changeSong(Constant.NEXT_SONG_INDEX)
    }

    override fun onPrevious() {
        changeSong(Constant.PREVIOUS_SONG_INDEX)
    }

    override fun onPlayPause() {
        if (service.isPlay()) {
            service.pause()
            imagePlay.setImageResource(R.drawable.ic_play)
        } else {
            service.start()
            imagePlay.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun createUIThread() {
        this.runOnUiThread(object : Runnable {
            override fun run() {
                MediaManager.mediaPlayer.let {
                    val currentPosition = MediaManager.mediaPlayer.currentPosition / 1000
                    seekBarDuration.progress = currentPosition
                    textPosition.text = MediaUltils.formatTime(currentPosition)
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun setUpSeekbar() {
        seekBarDuration.max = MediaManager.mediaPlayer.duration / 1000
        val durationTotal = MediaManager.songs[MediaManager.index].duration / 1000
        textDuration.text = MediaUltils.formatTime(durationTotal)
        createUIThread()
    }

    private fun updateSong() {
        textSongName.text = MediaManager.songs[MediaManager.index].name
        textArtist.text = MediaManager.songs[MediaManager.index].artist
        imagePlay.setImageResource(R.drawable.ic_pause)
        setUpSeekbar()
    }

    private fun changeSong(indexSong: Int) {
        service.changeSong(indexSong)
        updateSong()

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, PlayMusicActivity::class.java)
    }
}
