package com.li.musicmp3.data.repository

import com.li.musicmp3.data.source.MusicDataSource
import com.li.musicmp3.data.source.local.MusicLocalDataSource
import com.li.musicmp3.data.source.local.OnLoadMusicCallBack

class MusicRepository private constructor(
    private val localDataSource: MusicDataSource
) : MusicDataSource {

    override fun getSong(callBack: OnLoadMusicCallBack) {
        localDataSource.getSong(callBack)
    }

    companion object {
        private var instance: MusicRepository? = null

        fun getInstance(localDataSource: MusicDataSource) =
            instance ?: MusicRepository(localDataSource).also { instance = it }
    }
}
