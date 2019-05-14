package com.mukaase.android.organa

//import javax.sound.midi.ShortMessage.CONTINUE
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.text.DecimalFormat


/**
 *
 *
 * @author Godfred Afful
 * @version
 * @since , 22/09/2017
 */
object FileUtils {
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

//    //
//    // https://stackoverflow.com/questions/2149785/get-size-of-folder-or-file/19877372#19877372
//    fun sizeOfDirectory(): Long {
//        val folder = Paths.get("src/test/resources")
//        val size = Files.walk(folder)
//            .filter({ p -> p.toFile().isFile() })
//            .mapToLong({ p -> p.toFile().length() })
//            .sum()
//
//        return size
//    }

    fun getFolderSize(folder: File): Long {
        var length: Long = 0
        val files = folder.listFiles()

        val count = files.size

        for (i in 0 until count) {
            if (files[i].isFile) {
                length += files[i].length()
            } else {
                length += getFolderSize(files[i])
            }
        }
        return length
    }


    fun getAlbumStorageDir(albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC
            ), albumName
        )
        if (!file.mkdirs()) {
            Log.e("FileUtils", "Directory not created")
        }
        return file
    }

    fun getAlbumStorageDir2(ctx: Context, albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(ctx.getExternalFilesDir(Environment.DIRECTORY_MUSIC), albumName)
        if (!file.mkdirs()) {
            Log.e("FileUtils", "Directory not created")
        }
        Log.i("FileUtils", "Directory created" + file)
        return file
    }

    fun getAlbumStorageDir3(ctx: Context, albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(ctx.getExternalFilesDir(null), albumName)
        if (!file.mkdirs()) {
            Log.e("FileUtils", "Directory not created")
        }
        Log.i("FileUtils", "Directory created" + file)
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
                    "${locations[FileUtils.SD_CARD]}/${uri.pathSegments[1].removePrefix("primary:")}"
                } else {
                    "${locations[FileUtils.EXTERNAL_SD_CARD]}/${uri.pathSegments[1].split(":")[1]}"
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

    // To get a location on external storage unique for your app
    //Environment#DIRECTORY_MUSIC}
//    fun appDirectory(context: DashboardActivity) = context.getExternalFilesDirs()

    // To find locations on internal storage for your app
    // Your app's files but goes with uninstalls
    // If external, will go with uninstall if using getExternalFilesDir().
    // Internals dont' require permissions
    fun internalStorage(context: Context) = context.filesDir

    fun externalStorage() = Environment.getExternalStorageDirectory()
    fun externalStorage1(context: Context) = Environment.getExternalStorageDirectory()


//    fun internalStorage(context: DashboardActivity) = context.getExternalFilesDirs()

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable4(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable2(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }


    fun whatsAppAudioDir() = File(externalStorage().path + "/WhatsApp/Media/WhatsApp Audio")

//    fun wazap(ctx: Activity): Boolean {
//        return File(externalStorage().path + "/WhatsApp/Media/WhatsApp Audio").canRead()
//    }

    fun getPublicAlbumStorageDir(albumName: String): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
//            Log.e(LOG_TAG, "Directory not created")
            println("Directory not created")
        }
        return file
    }

    //    fun publicMusicDir(context: Context) = context.getExternalFilesDir(DIRECTORY_MUSIC)
    fun publicMusicDir() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

    // https://www.baeldung.com/java-folder-size
    fun folderSize(directory: File): Long {
        var length: Long = 0
        for (file in directory.listFiles()) {
            if (file.isFile)
                length += file.length()
            else
                length += folderSize(file)
        }
        return length
    }

    fun getFilesFromPath(path: String, showHiddenFiles: Boolean = false, onlyFolders: Boolean = false): List<File> {
        val file = File(path)
        return file.listFiles()
            .filter { showHiddenFiles || !it.name.startsWith(".") }
            .filter { !onlyFolders || it.isDirectory }
            .toList()
    }

//    fun getFileModelsFromFiles(files: List<File>): List<FileModel> {
//        return files.map {
//            FileModel(it.path, FileType.getFileType(it), it.name, convertFileSizeToMB(it.length()), it.extension, it.listFiles()?.size
//                ?: 0)
//        }
//    }

    fun convertFileSizeToMB(sizeInBytes: Long): Double {
        return (sizeInBytes.toDouble()) / (1024 * 1024)
    }

//    /**
//     * Attempts to calculate the size of a file or directory.
//     *
//     *
//     *
//     * Since the operation is non-atomic, the returned value may be inaccurate.
//     * However, this method is quick and does its best.
//     * https://stackoverflow.com/questions/2149785/get-size-of-folder-or-file/19877372#19877372
//     * API 26
//     */
//    fun size(path: Path): Long {
//
//        val size = AtomicLong(0)
//
//        try {
//            Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
//                fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
//
//                    size.addAndGet(attrs.size())
//                    return FileVisitResult.CONTINUE
//                }
//
//                fun visitFileFailed(file: Path, exc: IOException): FileVisitResult {
//
//                    println("skipped: $file ($exc)")
//                    // Skip folders that can't be traversed
//                    return FileVisitResult.CONTINUE
//                }
//
//                fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
//
//                    if (exc != null)
//                        println("had trouble traversing: $dir ($exc)")
//                    // Ignore errors traversing a folder
//                    return FileVisitResult.CONTINUE
//                }
//            })
//        } catch (e: IOException) {
//            throw AssertionError("walkFileTree will not throw IOException if the FileVisitor does not")
//        }
//        return size.get()
//    }

    val EXTERNAL_SD_CARD = "externalSdCard"
    val SD_CARD = "sdCard"
    val TAG = "FileUtils"
}