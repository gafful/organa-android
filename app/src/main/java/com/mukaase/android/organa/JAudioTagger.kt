package com.mukaase.android.organa

import org.jaudiotagger.audio.*
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import java.io.File

class JAudioTagger(): Tagger {
//    private val tag: org.jaudiotagger.tag.Tag
//
//    init {
//        val audioFile = AudioFileIO.read(file)
//        tag = audioFile.tag
//    }

    override fun title(tag: Tag) = tag.getFirst(FieldKey.TITLE)

    override fun artist(tag: Tag) = tag.getFirst(FieldKey.ARTIST)

    override fun album(tag: Tag) = tag.getFirst(FieldKey.ARTIST)

    override fun metadata(file: File): AudioMetadata {
        println("neeext: $file")///storage/emulated/0/WhatsApp/Media/WhatsApp Audio/AUD-20190511-WA0002.m4a
        // Unable to determine start of audio in file
        val tag = AudioFileIO.read(file).tag ?: return AudioMetadata(file.name)
        //        println("tag: $tag")
        return AudioMetadata(file.name, title(tag), artist(tag), album(tag))
    }
//    override fun metadata(file: File) = AudioMetadata(title(), artist())

//    private fun getMetaData(file: File): AudioMeta? {
//        Log.d(TAG, "getMetaData: " + file)
//        var audioMeta: AudioMeta? = null
//        try {
//            val audioFile = AudioFileIO.read(file)
//            Log.d(TAG, "audioFile: " + audioFile)
//
//            val tag = audioFile.tag
//            var artiste = tag.getFirst(FieldKey.ARTIST)
//            val title = tag.getFirst(FieldKey.TITLE)
//
//            Log.d(TAG, "tag: " + tag)
//            Log.d(TAG, "artiste: " + artiste)
//            Log.d(TAG, "title: " + title)
//
//            if((artiste.isEmpty() && !title.isEmpty()) || artiste.contains("Unknown", true)){
//                artiste = "Unknown"
//            }
//            if (!artiste.isEmpty()) {
//                Log.d(TAG, "GOTCHA ARTISTE")
//                audioMeta = AudioMeta(artiste, "", title, "", "")
//            } else {
//                Log.d(TAG, "EMPTY ARTISTE")
//            }
//        } catch (e: Exception) {
//            Log.d(TAG, "EMPTY ARTISTE EXCEPTION")
//            e.printStackTrace()//CannotReadException
//        }
//        Log.d(TAG, "audioMeta " + audioMeta)
//        return audioMeta
//    }
}