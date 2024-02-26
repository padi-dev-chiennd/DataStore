package com.example.datastore.data

import android.net.Uri

data class Video(
    val id: Long,
    val uri: Uri,
    var name: String,
    val duration: Int,
    val size: Int
)