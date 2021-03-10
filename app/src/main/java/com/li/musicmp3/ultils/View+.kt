package com.li.musicmp3.ultils

import android.view.View

fun View.OnClickListener.multiViewClick(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}
