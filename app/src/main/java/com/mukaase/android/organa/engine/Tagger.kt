package com.mukaase.android.organa

import com.mukaase.android.organa.data.AudioMetadata
import org.jaudiotagger.tag.Tag
import java.io.File

interface Tagger {
    fun title(tag: Tag): String
    fun artist(tag: Tag): String
    fun album(tag: Tag): String
    fun metadata(file: File): AudioMetadata
    fun skipped(): Boolean
}