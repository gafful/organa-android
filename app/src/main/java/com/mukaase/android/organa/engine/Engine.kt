package com.mukaase.android.organa.engine

import com.mukaase.android.organa.data.AudioMetadata
import com.mukaase.android.organa.data.EngineStats
import com.mukaase.android.organa.util.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Engine(private val tagger: Tagger) {

    private lateinit var srcFile: File
//    private lateinit var destFile: File
    private lateinit var currentFile: File

    init {
//        logD("Awwwwesoooomme---")
    }

    suspend fun start(audioCount: Int, src: File): Sequence<EngineStats> {
        require(src.isDirectory) { "Source file must be a directory!" }
        require(src.isDirectory) { "Destination file must be a directory!" }

        srcFile = src
//        destFile = dest
        var skipped = 0
        var cleaned = 0
        var metadata: AudioMetadata

        return withContext(Dispatchers.IO) {srcFile
            .walkTopDown()
            .filter { !it.isDirectory && !it.isHidden }
            .filter {
                it.extension.equals("mp3", true) or
                        it.extension.equals("m4a", true) or
                        it.extension.equals("wma", true)
            }
            .mapIndexed { index, file ->
                // TODO: Stop at 10,000
                currentFile = file
                metadata = tagger.metadata(file)
                if (tagger.skipped()) skipped += 1 else cleaned += 1
                logD("skippp: $skipped --- $cleaned --- ${index.toFloat()+1} --- $audioCount --- ${index.toFloat().div(audioCount)}")
                EngineStats(metadata, cleaned, skipped, percent((index + 1), audioCount))
            }
        }
    }

    private fun percent(nom: Int, denom: Int): Float {
        return nom.toFloat().div(denom).times(100)
    }
}