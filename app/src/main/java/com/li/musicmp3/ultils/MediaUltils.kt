package com.li.musicmp3.ultils

object MediaUltils {
    fun formatTime(currentPosition: Int): String {
        val seconds = (currentPosition % 60).toString()
        val minutes = (currentPosition / 60).toString()
        val durationIn = "$minutes:$seconds"
        val durationOut = "$minutes:0$seconds"
        return if (minutes.length == 1) {
            durationIn
        } else {
            durationOut
        }
    }
}
