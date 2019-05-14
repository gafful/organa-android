package com.mukaase.android.organa

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
//import com.mukaase.android.organa.FileExt.Companion.SD_CARD
import java.io.File
import java.text.DecimalFormat


/**
 *
 *
 * @author Godfred Afful
 * @version
 * @since , 22/09/2017
 */
object FileExt {
    //        println("1: ${filesDir}")// /data/user/0/com.mukaase.android.organa/files
//        println("2: ${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")// /storage/emulated/0/Android/data/com.mukaase.android.organa/files/Pictures
//    println("3: ${Environment.getExternalStorageDirectory()}")// /storage/emulated/0
//    println("4: ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}")// /storage/emulated/0/Pictures
    //println("root-i: ${Environment.getRootDirectory()}")
    //        externalCacheDirs.forEach {
    //
    //            println("521: ${it}")
    //        }
    //        getExternalFilesDirs(Environment.DIRECTORY_PICTURES).forEach {
    //
    //            println("5: ${it.path}")
    //            ///storage/emulated/0/Android/data/com.mukaase.android.organa/files
    //            //5: /storage/6362-6134/Android/data/com.mukaase.android.organa/files
    //        }


    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    fun getAlbumStorageDir(albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC
            ), albumName
        )
        if (!file.mkdirs()) {
            Log.e("FileExt", "Directory not created")
        }
        return file
    }

    fun getAlbumStorageDir2(ctx: Context, albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(ctx.getExternalFilesDir(Environment.DIRECTORY_MUSIC), albumName)
        if (!file.mkdirs()) {
            Log.e("FileExt", "Directory not created")
        }
        Log.i("FileExt", "Directory created" + file)
        return file
    }

    fun getAlbumStorageDir3(ctx: Context, albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(ctx.getExternalFilesDir(null), albumName)
        if (!file.mkdirs()) {
            Log.e("FileExt", "Directory not created")
        }
        Log.i("FileExt", "Directory created" + file)
        return file
    }

    /*If none of the pre-defined sub-directory names suit your files,
    you can instead call getExternalFilesDir() and pass null.
    This returns the root directory for your app's private directory on the external storage.*/


    //    val SD_CARD = "sdCard"
//    val EXTERNAL_SD_CARD = "externalSdCard"
    // https://stackoverflow.com/questions/39850004/android-get-all-external-storage-path-for-all-devices
    private val ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE"

    fun getAllStorageLocations(): Map<String, File> {
        val storageLocations = HashMap<String, File>(10)
        val sdCard = Environment.getExternalStorageDirectory()
        storageLocations.put(SD_CARD, sdCard)
        val rawSecondaryStorage = System.getenv(ENV_SECONDARY_STORAGE)
        if (!TextUtils.isEmpty(rawSecondaryStorage)) {
            val externalCards = rawSecondaryStorage.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in externalCards.indices) {
                val path = externalCards[i]
                storageLocations.put(EXTERNAL_SD_CARD + String.format(if (i == 0) "" else "_%d", i), File(path))
            }
        }
        return storageLocations
    }

    fun readableFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / Math.pow(
                1024.0,
                digitGroups.toDouble()
            )
        ) + " " + units[digitGroups]
    }

    fun resolveSingleSendIntentPath(intent: Intent): String {
        //file:///storage/emulated/0/WhatsApp/Media/WhatsApp%20Audio/AUD-20171026-WA0005.mp3
        return intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM).toString().removePrefix("file://")
    }

    fun resolvePath(uri: Uri?): String {
        Log.d(TAG, "resolvePath: " + uri)
        var path = ""

        if (null == uri) {
            Log.d(TAG, "null == uri")
            return path
        }

        when (uri.scheme) {
            "file" -> {
                path = uri.path
            }
            "content" -> {

                // content://com.android.externalstorage.documents/tree/primary%3AWhatsApp%2FMedia%2FWhatsApp%20Audio
                // content://com.android.externalstorage.documents/tree/9016-4EF8%3AGodDey%2Fmusic
                Log.d(TAG, "uri: " + uri)

                //content://com.android.externalstorage.documents/tree/primary%3AWhatsApp%2FMedia%2FWhatsApp%20Audio/document/primary%3AWhatsApp%2FMedia%2FWhatsApp%20Audio
                // content://com.android.externalstorage.documents/tree/9016-4EF8%3AGodDey%2Fmusic/document/9016-4EF8%3AGodDey%2Fmusic
//        Log.d(TAG, "docUri: " + docUri)

                //[tree, primary:WhatsApp/Media/WhatsApp Audio]
                //[tree, 9016-4EF8:GodDey/music]
                //[storage, emulated, 0, WhatsApp, Media, WhatsApp Audio, AUD-20171103-WA0004.mp3]
                Log.d(TAG, "pathSegments: " + uri.pathSegments)
                Log.d(TAG, "pathSegments: " + uri.pathSegments[0])//storage // tree
                Log.d(TAG, "pathSegments: " + uri.pathSegments[1])//emulated // downloads


                val locations = getAllStorageLocations()
                Log.d(TAG, "locations: " + locations)
                //{sdCard=/storage/emulated/0} emulator

                // sdcard1/GodDey/music
                // 0/WhatsApp/Media/WhatsApp Audio
                path = if ("primary:" in uri.pathSegments[1]) {
                    "${locations[FileExt.SD_CARD]}/${uri.pathSegments[1].removePrefix("primary:")}"
                } else {
                    "${locations[FileExt.EXTERNAL_SD_CARD]}/${uri.pathSegments[1].split(":")[1]}"
                }
            }
            else -> {
                path = ""
            }
        }
        Log.d(TAG, "resolvePath: " + path)
        return path

    }

    // change to single external intent
    fun isExternalIntent(intent: Intent?): Boolean {
        return "audio/mpeg" == intent?.type
    }

    fun isMultipleSendIntent(intent: Intent?): Boolean {
        return ("audio/mpeg" == intent?.type) && (intent.action == Intent.ACTION_SEND_MULTIPLE)
    }

//    fun isWritable(intent: Intent?): Boolean {
//        when(intent?.scheme){
//            ""
//        }
//        return ("audio/mpeg" == intent?.type) && (intent.action == Intent.ACTION_SEND_MULTIPLE)
//    }

    val EXTERNAL_SD_CARD = "externalSdCard"
    val SD_CARD = "sdCard"
    val TAG = "FileExt"
}