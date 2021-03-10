package com.li.musicmp3.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.li.musicmp3.R
import com.li.musicmp3.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter(
    private val handleClick: (position: Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songs = mutableListOf<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view, handleClick)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(songs[position])
    }

    fun updateData(songs: List<Song>) {
        this.songs = songs.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(
        itemView: View,
        private var handleClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                handleClick(adapterPosition)
            }
        }

        fun onBind(song: Song) {
            itemView.apply {
                textSongName.text = song.name
                textArtist.text = song.artist
            }
        }
    }

}
