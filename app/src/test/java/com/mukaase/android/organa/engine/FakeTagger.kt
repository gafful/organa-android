package com.mukaase.android.organa.engine

import com.mukaase.android.organa.data.AudioMetadata
import org.jaudiotagger.tag.Tag
import java.io.File

class FakeTagger: Tagger{
    override fun title(tag: Tag): String {
        return "Fake title"
    }

    override fun artist(tag: Tag): String {
        return "Fake title"
    }

    override fun album(tag: Tag): String {
        return "Fake title"
    }

    override fun metadata(file: File): AudioMetadata {
        return AudioMetadata("fileName", "title", "artist", "album")
    }

    override fun skipped(): Boolean {
        return true
    }

}