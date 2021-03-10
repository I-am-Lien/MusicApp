package com.li.musicmp3.data.source.local

import android.content.ContentResolver
import android.provider.MediaStore
import com.li.musicmp3.data.model.Song
import com.li.musicmp3.data.source.MusicDataSource
import com.li.musicmp3.data.source.local.ultil.LoadMusicTask

class MusicLocalDataSource private constructor(
    private val contentResolver: ContentResolver
) : MusicDataSource {

    override fun getSong(callBack: OnLoadMusicCallBack) {
        LoadMusicTask({ getSongFromDevice() }, callBack).execute()
    }

    private fun getSongFromDevice(): List<Song> {
        val songs = mutableListOf<Song>()
        val cursor =
            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )
        cursor?.let {
            while (it.moveToNext()) {
                songs.add(Song(it))
            }
            cursor.close()
        }
        return songs
    }

    companion object {
        private var instance: MusicLocalDataSource? = null

        fun getInstance(contentResolver: ContentResolver) =
            instance ?: MusicLocalDataSource(contentResolver).also { instance = it }
    }
}
