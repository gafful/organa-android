package com.mukaase.android.organa

import android.content.Context
import android.os.Environment.*
import java.io.File

object Utils {

    // To get a location on external storage unique for your app
    //Environment#DIRECTORY_MUSIC}
//    fun appDirectory(context: DashboardActivity) = context.getExternalFilesDirs()

    // To find locations on internal storage for your app
    // Your app's files but goes with uninstalls
    // If external, will go with uninstall if using getExternalFilesDir().
    // Internals dont' require permissions
    fun internalStorage(context: Context) = context.filesDir

    fun externalStorage() = getExternalStorageDirectory()
    fun externalStorage1(context: Context) = getExternalStorageDirectory()


//    fun internalStorage(context: DashboardActivity) = context.getExternalFilesDirs()

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        return getExternalStorageState() == MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        return getExternalStorageState() in
                setOf(MEDIA_MOUNTED, MEDIA_MOUNTED_READ_ONLY)
    }


    fun whatsAppAudioDir() = File(externalStorage().path + "/WhatsApp/Media/WhatsApp Audio")

//    fun wazap(ctx: Activity): Boolean {
//        return File(externalStorage().path + "/WhatsApp/Media/WhatsApp Audio").canRead()
//    }

    fun getPublicAlbumStorageDir(albumName: String): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(
            getExternalStoragePublicDirectory(
                DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
//            Log.e(LOG_TAG, "Directory not created")
            println("Directory not created")
        }
        return file
    }

    //    fun publicMusicDir(context: Context) = context.getExternalFilesDir(DIRECTORY_MUSIC)
    fun publicMusicDir() = getExternalStoragePublicDirectory(DIRECTORY_MUSIC)
}