package com.li.musicmp3.data.model

import android.database.Cursor
import android.provider.MediaStore

class Song(var name: String, var path: String, var artist: String, var duration: Int) {

    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)),
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)),
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)),
        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
    )
}
