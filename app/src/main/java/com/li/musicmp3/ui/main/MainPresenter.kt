package com.li.musicmp3.ui.main

import com.li.musicmp3.data.model.Song
import com.li.musicmp3.data.repository.MusicRepository
import com.li.musicmp3.data.source.local.OnLoadMusicCallBack

class MainPresenter(
    private val repository: MusicRepository,
    private val view: MainActivityContract.View
) : MainActivityContract.Presenter {

    override fun loadSongs() {
        repository.getSong(object : OnLoadMusicCallBack {
            override fun onLoadFinished(songs: List<Song>) {
                view.showSongs(songs)
            }

            override fun onLoadError(message: String) {
                view.showError(message)
            }
        })
    }
}
