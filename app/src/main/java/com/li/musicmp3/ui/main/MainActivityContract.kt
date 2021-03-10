package com.li.musicmp3.ui.main

import com.li.musicmp3.data.model.Song

interface MainActivityContract {
    interface Presenter {
        fun loadSongs()
    }

    interface View {
        fun showSongs(list: List<Song>)
        fun showError(message: String)
    }
}
