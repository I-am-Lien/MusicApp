package com.li.musicmp3.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.li.musicmp3.R
import com.li.musicmp3.data.repository.MusicRepository
import com.li.musicmp3.data.source.local.MusicLocalDataSource
import com.li.musicmp3.ultils.Constant
import com.li.musicmp3.ultils.MediaManager
import com.li.musicmp3.ui.play.PlayMusicActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.li.musicmp3.data.model.Song

class MainActivity : AppCompatActivity(),
    MainActivityContract.View {
    private val songAdapter = SongAdapter(handleClick = { position -> onSongClick(position) })
    private var presenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter =
            MainPresenter(
                MusicRepository.getInstance(MusicLocalDataSource.getInstance(this.contentResolver)),
                this
            )
        checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter?.loadSongs()
        } else {
            finish()
        }
    }

    override fun showSongs(list: List<Song>) {
        MediaManager.songs.addAll(list)
        songAdapter.updateData(list)
        recycleSongList.apply {
            adapter = songAdapter
            setHasFixedSize(true)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            presenter?.loadSongs()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constant.REQUEST_CODE
            )
        }
    }


    private fun onSongClick(position: Int) {
        startActivity(PlayMusicActivity.getIntent(this))
        MediaManager.index = position
    }

}
