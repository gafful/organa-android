package com.mukaase.android.organa

import java.io.File

class Engine(private val tagger: Tagger) {

    private lateinit var srcFile: File
    private lateinit var destFile: File
    private lateinit var currentFile: File

    init {
        println("Awwwwesoooomme---")
    }

    suspend fun start(src: File, dest: File): Sequence<AudioMetadata> {
        require(src.isDirectory) {"Source file must be a directory!"}
        require(src.isDirectory) {"Destination file must be a directory!"}
//        if (!src.isDirectory)
//            throw IllegalStateException("Source file must be a directory!")
//
//        if (!dest.isDirectory)
//            throw IllegalStateException("Destination file must be a directory!")

        srcFile = src
        destFile = dest

        return srcFile
            .walkTopDown()
            .filter { !it.isDirectory && !it.isHidden }
            .filter {
                it.extension.equals("mp3", true) or
                        it.extension.equals("m4a", true) or
                        it.extension.equals("wma", true)
            }.map {
                // TODO: Stop at 10,000
                currentFile = it
                tagger.metadata(it)
            }

    }

}