package com.mukaase.android.organa.console

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukaase.android.organa.R
import com.mukaase.android.organa.data.EngineStats
import com.mukaase.android.organa.engine.Engine
import com.mukaase.android.organa.util.FileUtils
import com.mukaase.android.organa.util.logD
import com.mukaase.android.organa.util.logI
import com.mukaase.android.organa.util.logW
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ConsoleViewModel(val engine: Engine) : ViewModel() {

    private val CHANNEL_ID = "wan"
    // A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
    // However ViewModel objects must never observe changes to lifecycle-aware observables, such as LiveData objects.
    private val REQUEST_CODE_CHOOSE_SOURCE_DIR = 2

    // Default: WhatsApp Audio
    // else: Root

    var srcFiles: MutableLiveData<Sequence<File>> = MutableLiveData()
    var srcStatus: MutableLiveData<String> = MutableLiveData()
    var srcDirName: MutableLiveData<String> = MutableLiveData()
    var srcDirPath: MutableLiveData<String> = MutableLiveData()
    var srcDirAudioFileCount: MutableLiveData<Int> = MutableLiveData(0)

    //    var srcDir: MutableLiveData<Directory> = MutableLiveData()
    var destDirName: MutableLiveData<String> = MutableLiveData()
    var destDirPath: MutableLiveData<String> = MutableLiveData()
    var destDirAvSpace: MutableLiveData<String> = MutableLiveData()
    var destStatus: MutableLiveData<String> = MutableLiveData()

    //    var timeElapsed: MutableLiveData<String> = MutableLiveData()
    var engineStats: MutableLiveData<EngineStats> = MutableLiveData()
    var engineStatsSequence: MutableLiveData<Sequence<EngineStats>> = MutableLiveData()

//    val d: Int by Delegates.observable(0,
//        { prop, old, new ->
//            if (new == 5) {
//                logD("fa ya-ya-ya-yahh $prop --- $old")
//            }
//        })
//    val sourceSet: Set<File> by Delegates.observable(
//        setOf(),
//        { prop, old, new ->
//            new.size
//        })

    /*For your app to display to the user without any visible pauses, the main thread has to update the screen every 16ms
    or more often, which is about 60 frames per second. Many common tasks take longer than this, such as parsing large
    JSON datasets, writing data to a database, or fetching data from the network.*/
    // assessSourceDirectory
//    @UiThread
    fun initSourceDirectory(
        source: File = FileUtils.whatsAppAudioDir(),
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        logD("initSourceDirectory")
        logD("source.canRead() ${source.canRead()} -- source.setReadable(true): ${source.setReadable(true)}")

        srcDirName.value = source.name
        srcDirPath.value = source.path
        srcStatus.value = CHECK_IN_PROGRESS

        if (!source.isDirectory) srcStatus.value = CHECK_FAIL
        if (!source.canRead() && !source.setReadable(true)) srcStatus.value = UNREADABLE


//        var audioCount = 0
//        var nonAudioCount = 0
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val srcFilesSet = source
                    .walkTopDown()
                    .filter { !it.isDirectory && !it.isHidden }
                    .filter {
                        it.extension.equals("mp3", true) or
                                it.extension.equals("m4a", true) or
                                it.extension.equals("wma", true)
                    }.mapIndexed { index, file ->
                        logD("file is: ${file}")
                        println("file is: ${file}")
                        srcDirAudioFileCount.postValue(index + 1)
                        file
                    }
                srcFiles.postValue(srcFilesSet)
            }
            srcStatus.postValue(CHECK_OK)
        }
    }

    @UiThread
    fun initDestDirectory(destDir: File = FileUtils.publicMusicDir()) {
        logD("initDestDirectory")
        destStatus.value = CHECK_IN_PROGRESS
        destDirName.value = destDir.name
        destDirPath.value = destDir.path
        destDirAvSpace.value = FileUtils.readableFileSize(destDir.freeSpace)
        logD("destDir.canWrite(): ${destDir.canWrite()} ---- destDir.setWritable(true): ${destDir.setWritable(true)}")
        if (destDir.canWrite() || destDir.setWritable(true)) destStatus.value = CHECK_OK else destStatus.value =
            UNWRITABLE
    }

    @SuppressLint("PrivateResource")
    fun openSourceDirectory(context: ConsoleActivity) {
        logD("openSourceDirectory")
        // Check if readable
        ChooserDialog(context)
            .withFilter(true, false)
            .withStartFile(File(srcDirPath.value).parent)
            .withResources(R.string.choose_source_folder, R.string.title_choose, R.string.dialog_cancel)
            .withChosenListener { path, pathFile ->
                logD("FILE: $path; PATHFILE: $pathFile")
                //FILE: /storage/emulated/0; PATHFILE: /storage/emulated/0

                initSourceDirectory(pathFile)
            }
            .withNavigateUpTo { true }
            .withNavigateTo { true }
            .build()
            .show()

//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
//            // Filter to only show results that can be "opened", such as a
//            // file (as opposed to a list of contacts or timezones)
////            addCategory(Intent.CATEGORY_OPENABLE)
//            putExtra("android.content.extra.SHOW_ADVANCED", true)
//
//            // Filter to show only images, using the image MIME data type.
//            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
//            // To search for all documents available via installed storage providers,
//            // it would be "*/*".
////            type = "audio/*"
//        }
//
//       context.startActivityForResult(intent, REQUEST_CODE_CHOOSE_SOURCE_DIR)
    }

    @SuppressLint("PrivateResource")
    fun openDestDirectory(context: ConsoleActivity) {
        logD("openDestDirectory")
        ChooserDialog(context)
            .withFilter(true, false)
            .withStartFile(File(destDirPath.value).parent)
            .withResources(R.string.choose_dest_folder, R.string.title_choose, R.string.dialog_cancel)
            .withChosenListener { _, pathFile ->
                initDestDirectory(pathFile)
            }
            .withNavigateUpTo { true }
            .withNavigateTo { true }
            .build()
            .show()
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
    fun start(audioCount: Int, sourceFile: File) {
        logD("start")
        // start with coroutine
        // when clearing and task not done, defer to WorkManager
        // Coroutines are useful here for when you have work that needs to be done only if the ViewModel is active.

//        ViewModel.viewModelScope.launch {
        // Walk through source
        // Get title, author & album
        // Get match and copy there and delete
        // Create by author, copy and delete
//        }

        viewModelScope.launch {
            //            val time = measureTimeMillis {
//                withContext(Dispatchers.IO) {
//                    try {

            // TODO: Pass them as arguments
            val infos = engine.start(audioCount, sourceFile)
            println("infos: $infos")
            engineStatsSequence.postValue(infos)
//                        infos.forEachIndexed { index, stats ->
//                            logD("are we there: $stats")
////                        engineStats.postValue(EngineStats(metadata, index, index, index.toFloat()))
//                            engineStats.postValue(stats)
//                        }
//                    } catch (e: Exception) {
//                        logE("$e")
//                        srcStatus.postValue(e.message)
//                    }
//                }
//            }
            // Which is faster:
            // new SimpleDateFormat("mm:ss:SSS")).format(new Date(time)
            // System.out.printf("%tT", millis-TimeZone.getDefault().getRawOffset());
//            String.format("%02d min, %02d sec",
//                TimeUnit.MILLISECONDS.toMinutes(millis),
//                TimeUnit.MILLISECONDS.toSeconds(millis) -
//                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//            );
//            val formattedTime = "${TimeUnit.MILLISECONDS.toMinutes(time)}:" +
//                    "${TimeUnit.MILLISECONDS.toSeconds(time)}\n" +
//                    "${TimeUnit.MILLISECONDS.toMillis(time)}"
//            logD(" formatttt: $formattedTime")
//            timeElapsed.postValue(formattedTime)
        }

//        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("title")
//            .setContentText("content")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//
//        // Create an explicit intent for an Activity in your app
//        val intent = Intent(ctx, ConsoleActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        // Caution: The integer ID that you give to startForeground() must not be 0.
//        val pendingIntent: PendingIntent =
//            Intent(ctx, ConsoleActivity::class.java).let { notificationIntent ->
//                PendingIntent.getActivity(ctx, 0, notificationIntent, 0)
//            }
//
//        val notification: Notification = Notification.Builder(ctx, CHANNEL_ID)
//            .setContentTitle("notification title")
//            .setContentText("content text")
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentIntent(pendingIntent)
//            .setTicker("ticker text")
//            .build()
//
//        with(NotificationManagerCompat.from(ctx)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(2, builder.build())
//        }
//
//        ctx.startForegroundService(intent)
////        ctx.startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

//    // Because you must create the notification channel before posting any notifications on Android 8.0 and higher,
//    // you should execute this code as soon as your app starts.
//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    companion object {
//        internal const val REQUEST_CODE_WRITE_EXT_STORAGE = 1
        internal const val CHECK_IN_PROGRESS = "CHECK_IN_PROGRESS"
        internal const val CHECK_OK = "CHECK_OK"
        internal const val CHECK_FAIL = "CHECK_FAIL"
        internal const val UNREADABLE = "UNREADABLE"
        internal const val UNWRITABLE = "UNWRITABLE"
    }
}