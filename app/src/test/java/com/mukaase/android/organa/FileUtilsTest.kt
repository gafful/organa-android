package com.mukaase.android.organa

import android.net.Uri
import com.mukaase.android.organa.util.FileUtils
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class FileUtilsTest {

//     @Test
//     fun resolvePathFromUri_content_scheme() {
//         // Given an empty Uri
//         val uri = Uri.EMPTY

//         // When the uri's path is resolved
//         //content://com.android.externalstorage.documents/document/6362-6134%3ADeitrick%20Haddon-Church%20on%20the%20moon%2FFolder.jpg
// //        val builder = Uri.Builder()
// //        builder.scheme("https")
// //            .authority("www.myawesomesite.com")
// //            .appendPath("turtles")
// //            .appendPath("types")
// //            .appendQueryParameter("type", "1")
// //            .appendQueryParameter("sort", "relevance")
// //            .fragment("section-name")
// //        val myUrl = builder.build().toString()
//         val path = FileUtils.resolvePath(uri)

//         // Then the path is sss
//         assertThat(path, `is`(""))
//     }

//    @Test
//    fun resolvePathFromUri_file_scheme() {
//        // Given 3 completed tasks and 2 active tasks
//        val uri = Uri.fromParts("", "", "")
//
//        // When the list of tasks is computed
//        val result = FileUtils.resolvePath(uri)
//
//        // Then the result is 40-60
//        assertThat(result.activeTasksPercent, `is`(40f))
//        assertThat(result.completedTasksPercent, `is`(60f))
//    }

    @Test
    fun getHumanReadableSize_bytes() {
        // Given a figure
        val sizeB = 12L
        val sizeKB = 1234L
        val sizeMB = 1223456L
        val sizeGB = 1234567890L

        // When the figure is computed
        val resultB = FileUtils.readableFileSize(sizeB)
        val resultKB = FileUtils.readableFileSize(sizeKB)
        val resultMB = FileUtils.readableFileSize(sizeMB)
        val resultGB = FileUtils.readableFileSize(sizeGB)

        // Then the result is right
        assertThat(resultB, `is`("12 B"))
        assertThat(resultKB, `is`("1.2 kB"))
        assertThat(resultMB, `is`("1.2 MB"))
        assertThat(resultGB, `is`("1.1 GB"))
    }

//    @Test
//    fun getHumanReadableSize_gigabytes() {
//        // Given 3 completed tasks and 2 active tasks
//        val size = 1234567890L
//
//        // When the list of tasks is computed
//        val result = FileUtils.readableFileSize(size)
//
//        // Then the result is 40-60
//        assertThat(result.activeTasksPercent, `is`(40f))
//        assertThat(result.completedTasksPercent, `is`(60f))
//    }

    // TODO: Framework
    // val result = FileUtils.getAllStorageLocations()
    // val result = FileUtils.externalStorage()
    // val result = FileUtils.publicMusicDir()
    // val result = FileUtils.whatsAppAudioDir()

}