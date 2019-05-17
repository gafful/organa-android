package com.mukaase.android.organa

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

class DashboardViewModel(val engine: Engine) : ViewModel() {

    private val CHANNEL_ID = "wan"
    // A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
    // However ViewModel objects must never observe changes to lifecycle-aware observables, such as LiveData objects.
    private val REQUEST_CODE_CHOOSE_SOURCE_DIR = 2

    // Default: WhatsApp Audio
    // else: Root
    var srcDirName: MutableLiveData<String> = MutableLiveData()
    var srcDirPath: MutableLiveData<String> = MutableLiveData()
    var srcDirAudioFileCount: MutableLiveData<Int> = MutableLiveData()
    var srcDir: MutableLiveData<Directory> = MutableLiveData()
    var destDirName: MutableLiveData<String> = MutableLiveData()
    var destDirPath: MutableLiveData<String> = MutableLiveData()
    var destDirAvSpace: MutableLiveData<String> = MutableLiveData()
    var engineStats: MutableLiveData<EngineStats> = MutableLiveData()
    var timeElapsed: MutableLiveData<String> = MutableLiveData()
    val d: Int by Delegates.observable(0,
        { prop, old, new ->
            if (new == 5){
                println("fa ya-ya-ya-yahh $prop --- $old")
            }
    })


    // FIXME: Keep Android code out of the view model.
    // FIXME: Views, fragments, activities, and contexts are Android specific code
    fun checkStoragePermissions(ctx: Activity) {
        println("checkStoragePermissions")
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    ctx,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    ctx,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXT_STORAGE
                )

                // REQUEST_CODE_WRITE_EXT_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            // Extra check for isReadable ...
            //TODO: Osei, ...
//            initSourceAndDestDirectories()
            setSourceDirectory()
            setDestDirectory()
        }

    }

    /*For your app to display to the user without any visible pauses, the main thread has to update the screen every 16ms
    or more often, which is about 60 frames per second. Many common tasks take longer than this, such as parsing large
    JSON datasets, writing data to a database, or fetching data from the network.*/
    @UiThread
    private fun setSourceDirectory(source: File = FileUtils.whatsAppAudioDir()) {
        println("setSourceDirectory")

        srcDirName.value = source.name
        srcDirPath.value = source.path

        var audioCount = 0
        var nonAudioCount = 0
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                source
                    .walkTopDown()
                    .filter { !it.isDirectory && !it.isHidden }
                    .filter {
                        it.extension.equals("mp3", true) or
                                it.extension.equals("m4a", true) or
                                it.extension.equals("wma", true)
                    }
                    .forEach {
                        println("ext is: ${it.extension}")
                        srcDirAudioFileCount.postValue(audioCount)
                    }
            }
        }
    }

    @UiThread
    private fun setDestDirectory(destDir: File = FileUtils.publicMusicDir()) {
        println("setDestDirectory")

        destDirName.value = destDir.name
        destDirPath.value = destDir.path
        destDirAvSpace.value = FileUtils.readableFileSize(destDir.freeSpace)
    }

    @SuppressLint("PrivateResource")
    fun openSourceDirectory(context: DashboardActivity) {
        println("openSourceDirectory")
        // Check if readable
        ChooserDialog(context)
            .withFilter(true, false)
            .withStartFile(File(srcDirPath.value).parent)
            .withResources(R.string.choose_source_folder, R.string.title_choose, R.string.dialog_cancel)
            .withChosenListener { path, pathFile ->
                println("FILE: $path; PATHFILE: $pathFile")
                //FILE: /storage/emulated/0; PATHFILE: /storage/emulated/0

                setSourceDirectory(pathFile)
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
    fun openDestDirectory(context: DashboardActivity) {
        println("openDestDirectory")
        ChooserDialog(context)
            .withFilter(true, false)
            .withStartFile(File(destDirPath.value).parent)
            .withResources(R.string.choose_dest_folder, R.string.title_choose, R.string.dialog_cancel)
            .withChosenListener { path, pathFile ->
                setDestDirectory(pathFile)
            }
            .withNavigateUpTo { true }
            .withNavigateTo { true }
            .build()
            .show()
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
    fun start(ctx: DashboardActivity) {
        println("start")
        // start with coroutine
        // when clearing and task not done, defer to WorkManager
        // Coroutines are useful here for when you have work that needs to be done only if the ViewModel is active.

//        ViewModel.viewModelScope.launch {
        // Walk through source
        // Get title, author & album
        // Get match and copy there and delete
        // Create by author, copy and delete
//        }

        GlobalScope.launch {
            val time = measureTimeMillis {
                withContext(Dispatchers.IO) {
                    val infos =
                        engine.start(srcDirAudioFileCount.value!!, File((srcDirPath.value)), File(destDirPath.value))
                    infos.forEachIndexed { index, stats ->
                        println("are we there: $stats")
//                        engineStats.postValue(EngineStats(metadata, index, index, index.toFloat()))
                        engineStats.postValue(stats)
                    }
                }
            }
            // Which is faster:
            // new SimpleDateFormat("mm:ss:SSS")).format(new Date(time)
            // System.out.printf("%tT", millis-TimeZone.getDefault().getRawOffset());
//            String.format("%02d min, %02d sec",
//                TimeUnit.MILLISECONDS.toMinutes(millis),
//                TimeUnit.MILLISECONDS.toSeconds(millis) -
//                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//            );
            val formattedTime = "${TimeUnit.MILLISECONDS.toMinutes(time)}:" +
                    "${TimeUnit.MILLISECONDS.toSeconds(time)}\n" +
                    "${TimeUnit.MILLISECONDS.toMillis(time)}"
            println(" formatttt: $formattedTime")
            timeElapsed.postValue(formattedTime)
        }

//        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("title")
//            .setContentText("content")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//
//        // Create an explicit intent for an Activity in your app
//        val intent = Intent(ctx, DashboardActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        // Caution: The integer ID that you give to startForeground() must not be 0.
//        val pendingIntent: PendingIntent =
//            Intent(ctx, DashboardActivity::class.java).let { notificationIntent ->
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


    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("onRequestPermissionsResult")
        when (requestCode) {
            REQUEST_CODE_WRITE_EXT_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val ok = File(Environment.getExternalStorageDirectory().path + "/0awaz").mkdir()
                    println("root-ok2: ${ok}")
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        println("onActivityResult")
        if (requestCode == REQUEST_CODE_CHOOSE_SOURCE_DIR && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                println("Uri: $uri")
                //content://com.android.externalstorage.documents/document/6362-6134%3ADeitrick%20Haddon-Church%20on%20the%20moon%2FFolder.jpg
                println("Uri pat: ${FileUtils.resolvePath(uri)}")
                File("${FileUtils.resolvePath(uri) + "/hopeaf"}")
//                Files.createDirectory(Path())
            }
        }
    }

    override fun onCleared() {
        println("onCleared")
        super.onCleared()
    }

    companion object {
        internal const val REQUEST_CODE_WRITE_EXT_STORAGE = 1
    }
}