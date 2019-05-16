package com.mukaase.android.organa

// filename, title, artiste, album
data class AudioMetadata(
    val fileName: String,
    val title: String = "",
    val artist: String = "",
    val album: String = ""
)