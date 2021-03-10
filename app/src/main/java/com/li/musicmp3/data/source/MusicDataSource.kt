package com.li.musicmp3.data.source

import com.li.musicmp3.data.source.local.OnLoadMusicCallBack

interface MusicDataSource {
    fun getSong(callBack: OnLoadMusicCallBack)
}
