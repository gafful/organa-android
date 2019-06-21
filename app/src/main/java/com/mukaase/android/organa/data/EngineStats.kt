package com.mukaase.android.organa.data

import com.mukaase.android.organa.data.AudioMetadata

data class EngineStats(val audioMetadata: AudioMetadata, val cleaned: Int, val skipped: Int, val progress: Float) {
}