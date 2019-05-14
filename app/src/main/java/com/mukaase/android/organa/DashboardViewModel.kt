package com.mukaase.android.organa

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.obsez.android.lib.filechooser.ChooserDialog
import java.io.File
//import kotlin.coroutines.CoroutineContext

class DashboardViewModel : ViewModel() {

    // A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
    // However ViewModel objects must never observe changes to lifecycle-aware observables, such as LiveData objects.
    private val REQUEST_CODE_CHOOSE_SOURCE_DIR = 2

    // Default: WhatsApp Audio
    // else: Root
    var srcDirName: MutableLiveData<String> = MutableLiveData()
    var srcDirPath: MutableLiveData<String> = MutableLiveData()
    var srcDirSize: MutableLiveData<String> = MutableLiveData()
    var destDirName: MutableLiveData<String> = MutableLiveData()
    var destDirPath: MutableLiveData<String> = MutableLiveData()
    var destDirAvSpace: MutableLiveData<String> = MutableLiveData()
//
//    init {
//        val whatsAppAudioDir = File("${Constants.WHATSAPP_DIR}")
//        val srcPath = if (whatsAppAudioDir.exists()) whatsAppAudioDir.path else FileUtils.externalStorage().path
//        srcDirectoryPath.value = srcPath
//    }

    fun initViews(){

    }

    fun checkStoragePermissions(ctx: Activity){
        println("checkStoragePermissions")
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ctx,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ctx,
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
            initSourceAndDestDirectories()
        }
    }

    private fun initSourceAndDestDirectories() {
        println("initSourceAndDestDirectories")
        // Source
        // If WA directory exists, use, else, root
        if (FileUtils.whatsAppAudioDir().canRead()){
            srcDirName.value = FileUtils.whatsAppAudioDir().name
            srcDirPath.value = FileUtils.whatsAppAudioDir().path
            srcDirSize.value = FileUtils.getFolderSize(FileUtils.whatsAppAudioDir()).toString()
        }

        // Dest
        if (FileUtils.publicMusicDir().canWrite()){
            destDirName.value = FileUtils.publicMusicDir().name
            destDirPath.value = FileUtils.publicMusicDir().path
            destDirAvSpace.value = FileUtils.readableFileSize(FileUtils.publicMusicDir().freeSpace)
        }
    }

    @SuppressLint("PrivateResource")
    fun openSourceDirectory(context: DashboardActivity) {
        println("openSourceDirectory")
        // Check if readable
        ChooserDialog(context)
            .withFilter(true, false)
            .withStartFile(FileUtils.externalStorage().path)
//            .withStartFile(Utils.externalStorage(context).path + Constants.WHATSAPP_DIR)
            .withResources(R.string.title_choose_folder, R.string.title_choose, R.string.dialog_cancel)
            .withChosenListener { path, pathFile ->
//                Toast.makeText(context, "FILE: $path; PATHFILE: $pathFile", Toast.LENGTH_SHORT).show()
                println("FILE: $path; PATHFILE: $pathFile")

                //_path = path
                //_tv.setText(_path)
                ////_iv.setImageURI(Uri.fromFile(pathFile));
                //_iv.setImageBitmap(ImageUtil.decodeFile(pathFile))
            }
            .withNavigateUpTo { true }
            .withNavigateTo { true }
            .build()
            .show()

        //        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
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
//        startActivityForResult(intent, REQUEST_CODE_CHOOSE_SOURCE_DIR)
    }

    @SuppressLint("PrivateResource")
    fun openDestDirectory(context: DashboardActivity) {
        println("openDestDirectory")
        ChooserDialog(context)
            .withFilter(true, false)
            .withStartFile(FileUtils.publicMusicDir().path)
            .withResources(R.string.title_choose_folder, R.string.title_choose, R.string.dialog_cancel)
            .withChosenListener { path, pathFile ->
//                Toast.makeText(context, "FILE: $path; PATHFILE: $pathFile", Toast.LENGTH_SHORT).show()
                println("FILE: $path; PATHFILE: $pathFile")
            }
//            .withNavigateUpTo { true }
            .withNavigateTo { true }
            .build()
            .show()
    }

    fun start() {
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
    }

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