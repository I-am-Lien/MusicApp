package com.li.musicmp3.data.source.local

import com.li.musicmp3.data.model.Song

interface OnLoadMusicCallBack {
    fun onLoadFinished(songs: List<Song>)
    fun onLoadError(message: String)
}
