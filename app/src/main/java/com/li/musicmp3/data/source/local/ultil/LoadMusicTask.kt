package com.li.musicmp3.data.source.local.ultil

import android.os.AsyncTask
import com.li.musicmp3.R
import com.li.musicmp3.data.model.Song
import com.li.musicmp3.data.source.local.OnLoadMusicCallBack
import java.lang.Exception

class LoadMusicTask(
    private val loadSong: () -> List<Song>,
    private val callback: OnLoadMusicCallBack
) : AsyncTask<Unit, Unit, List<Song>>() {

    private var exception: Exception? = null
    override fun doInBackground(vararg p0: Unit?): List<Song> =
        try {
            loadSong()
        } catch (error: Exception) {
            exception = error
            emptyList()
        }


    override fun onPostExecute(result: List<Song>?) {
        super.onPostExecute(result)
        result?.let {
            callback.onLoadFinished(it)
        } ?: callback.onLoadError(exception.toString())
    }
}
