package com.li.musicmp3.ultils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.li.musicmp3.data.model.Song

object MediaManager {
    var context: Context? = null
    var mediaPlayer: MediaPlayer = MediaPlayer()
    var index = 0
    val songs = ArrayList<Song>()

    fun create(index: Int) {
        release()
        MediaManager.index = index
        val song = songs[index]
        mediaPlayer = MediaPlayer.create(context, Uri.parse(song.path))
        start()
    }

    fun start() = mediaPlayer.let { mediaPlayer.start() }
    fun stop() = mediaPlayer.let { mediaPlayer.stop() }
    fun pause() = mediaPlayer.let { mediaPlayer.pause() }
    fun changeSong(value: Int) {
        index += value
        if (index >= songs.size) {
            index = 0
        } else if (index < 0) {
            index = songs.size - 1
        }
        create(index)
    }

    fun isPlay(): Boolean = mediaPlayer.isPlaying

    private fun release() = mediaPlayer.let { mediaPlayer.release() }

}

