package com.mukaase.android.organa

data class EngineStats(val audioMetadata: AudioMetadata, val cleaned: Int, val skipped: Int, val progress: Float) {
}