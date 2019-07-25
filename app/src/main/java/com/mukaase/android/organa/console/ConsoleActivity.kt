package com.mukaase.android.organa.console

//import android.text.Html.FROM_HTML_MODE_LEGACY
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mukaase.android.organa.AnimatorListenerAdapter
import com.mukaase.android.organa.R
import com.mukaase.android.organa.util.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

class ConsoleActivity : AppCompatActivity() {
    private lateinit var viewModel: ConsoleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        logD("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val vmFactory = InjectorUtils.provideConsoleViewModel()
        viewModel = ViewModelProviders.of(this, vmFactory).get(ConsoleViewModel::class.java)
        initObservers()
        viewModel.initSourceDirectory()
        checkStoragePermissions()
    }

    private fun initObservers() {
        logD("initObservers")

        // Source
        viewModel.srcDirName.observe(this, Observer {
            val text = when (it) {
                ConsoleViewModel.DEFAULT_SOURCE_FOLDERS_NOT_FOUND -> getString(R.string.unknown)
                else -> it
            }
            dashboard_src_panel_name.text = text
        })
        viewModel.srcDirPath.observe(this, Observer {
            val text = when (it) {
                ConsoleViewModel.DEFAULT_SOURCE_FOLDERS_NOT_FOUND -> getString(R.string.unknown)
                else -> it
            }
            dashboard_src_panel_path.text = text
        })
        viewModel.srcDirAudioFileCount.observe(this, Observer {
            dashboard_src_panel_file_count.text = "$it files"
//            dashboard_src_panel_file_count.text = getString(R.string.num_str, getString(R.string.files))
        })

        // Destination
        viewModel.destDirName.observe(this, Observer {
            val text = when (it) {
                ConsoleViewModel.DEST_FOLDER_INVALID -> getString(R.string.unknown)
                else -> it
            }
            dashboard_dest_panel_name.text = text
        })
        viewModel.destDirPath.observe(this, Observer {
            val text = when (it) {
                ConsoleViewModel.DEST_FOLDER_INVALID -> getString(R.string.unknown)
                else -> it
            }
            dashboard_dest_panel_path.text = text
        })
        viewModel.destDirAvSpace.observe(this, Observer {
            val text = when (it) {
                ConsoleViewModel.DEST_FOLDER_INVALID -> getString(R.string.unknown)
                else -> it
            }
            dashboard_dest_panel_file_count.text = text
        })


        // Display
        viewModel.srcStatus.observe(this, Observer { status ->
            logD("display10: $status")
            val display1 = when (status) {
                ConsoleViewModel.CHECK_OK -> getString(R.string.src_msg, getString(R.string.ok))
                ConsoleViewModel.DEFAULT_SOURCE_FOLDERS_NOT_FOUND -> getString(R.string.src_msg, getString(R.string.default_source_not_found))
//                ConsoleViewModel.UNREADABLE -> getString(R.string._unreadable, "SOURCE")
                else -> getString(R.string.src_msg, getString(R.string.unknown))
            }
            logD("display11: $display1")
            dashboard_display_1.text = display1
        })

        viewModel.destStatus.observe(this, Observer { status ->
            val display2 = when (status) {
                ConsoleViewModel.CHECK_OK -> getString(R.string.dest_msg, getString(R.string.ok))
                ConsoleViewModel.DEST_FOLDER_INVALID -> getString(R.string.dest_msg, getString(R.string.no_default_dest_folder))
                else -> getString(R.string.dest_msg, getString(R.string.unknown))
            }
            dashboard_display_2.text = display2
        })

        if (dashboard_power_switch.isEnabled) {
            viewModel.engineStats.observe(this, Observer {
                //            counter_cleaned.text = it.cleaned.toString()
//            counter_skipped.text = it.skipped.toString()
//            counter_mp3.text = it.progress.toString()
//            counter_remaining.text = getString(R.string.precision_1, it.progress, "%")

                dashboard_display_1.text = getString(R.string.scanning_, it.audioMetadata.fileName)
                dashboard_display_2.text = getString(R.string.title_, it.audioMetadata.title)
                dashboard_display_3.text = getString(R.string.artist_, it.audioMetadata.artist)
                dashboard_display_4.text = getString(R.string.album_, it.audioMetadata.album)
                dashboard_display_5.text = getString(R.string.scanning_, it.audioMetadata.fileName)

                logD("monitoring the progress: ${it.progress}")

                if (it.progress == 100.0f) {
                    dashboard_gears.cancelAnimation()
                    dashboard_gears_outer.cancelAnimation()
//                dashboard_progress_indicator.cancelAnimation()
                    dashboard_power_switch.removeAllAnimatorListeners()
                    dashboard_power_switch.setMinAndMaxFrame(1, 11)
                    dashboard_power_switch.frame = 1
//                dashboard_power_switch.playAnimation()
//                counter_remaining.text = "100%"
//
//                chronometer.stop()

                    dashboard_display_1.text = getString(R.string.scan_complete)
                    dashboard_display_2.text = formatHtmlString(R.string.title_, it.audioMetadata.title)
                    dashboard_display_3.text = getString(R.string.artist_, it.audioMetadata.artist)
                    dashboard_display_4.text = getString(R.string.album_, it.audioMetadata.album)
                    dashboard_display_5.text = getString(R.string.scanning_, it.audioMetadata.fileName)
                }
            })
        }

//        viewModel.timeElapsed.observe(this, Observer {
//            counter_duration.text = it
//        })
    }

    override fun onDestroy() {
        logD("onDestroy")
        super.onDestroy()
    }

    private fun initViews() {
        logD("initViews")

        //TODO: Do within a coroutine
        dashboard_power_switch.frame = 35

//        counter_remaining_label.isSelected = true
//        counter_cleaned_label.isSelected = true
//        counter_skipped_label.isSelected = true
//        counter_duration_label.isSelected = true
        dashboard_src_panel_name.isSelected = true
//        dashboard_src_panel_path.isSelected = true
        dashboard_dest_panel_name.isSelected = true
//        dashboard_dest_panel_path.isSelected = true

//        logD("wan2: ${Environment.getExternalStorageDirectory()}")
//        getExternalFilesDirs(null).forEach({
//            logD("it: $it")
//        })
//        logD("two: ${filesDir}")
//        2019-05-15 20:54:20.108 14345-14345/com.mukaase.android.organa I/System.out: wan2: /storage/emulated/0
//        2019-05-15 20:54:20.114 14345-14345/com.mukaase.android.organa I/System.out: wan: [Ljava.io.File;@90b0e5
//        2019-05-15 20:54:20.114 14345-14345/com.mukaase.android.organa I/System.out: two: /data/user/0/com.mukaase.android.organa/files
    }

    fun checkStoragePermissions() {
        logD("checkStoragePermissions")
        if (ContextCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXT_STORAGE
                )
            }
        } else {
            // Permission has already been granted
            // TODO: Run at the same time
            viewModel.initDestDirectory()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        logD("onRequestPermissionsResult")
        when (requestCode) {
            REQUEST_CODE_WRITE_EXT_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    logI("WRITE_EXT_STORAGE permissions granted")
                    viewModel.initDestDirectory()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    logW("Permissions not granted")
                    dashboard_display_2.text = getString(R.string.dest_msg, getString(R.string.no_write_permissions))
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        logD("onActivityResult")
        if (requestCode == REQUEST_CODE_WRITE_EXT_STORAGE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                logD("Uri: $uri")
                //content://com.android.externalstorage.documents/document/6362-6134%3ADeitrick%20Haddon-Church%20on%20the%20moon%2FFolder.jpg
                logD("Uri pat: ${FileUtils.resolvePath(uri)}")
                File(FileUtils.resolvePath(uri) + "/hopeaf")
//                Files.createDirectory(Path())
            }
        }
    }

    fun onSourceDirBtnClick(@Suppress("UNUSED_PARAMETER") v: View) {
        logD("onSourceDirBtnClick")
        viewModel.openSourceDirectory(this)
    }

    fun onDestDirBtnClick(@Suppress("UNUSED_PARAMETER") v: View) {
        logD("onDestDirBtnClick")
        viewModel.openDestDirectory(this)
    }

    fun onPowerSwitch(@Suppress("UNUSED_PARAMETER") v: View) {
        logD("onPowerSwitch")

        if (ConsoleViewModel.CHECK_OK != viewModel.srcStatus.value ||
            ConsoleViewModel.CHECK_OK != viewModel.destStatus.value
        ) return

        val animatorListener = AnimatorListenerAdapter(
            onStart = { },
            onEnd = {
                //                playButton.isActivated = false
//                animationView.performanceTracker?.logRenderTimes()
//                updateRenderTimesPerLayer()
//                runGears()
//                logD("wee dunn!!!")
//                chronometer.start()
                viewModel.start(viewModel.srcDirAudioFileCount.value!!, File(viewModel.srcDirPath.value))
                runGears()
//                updateDisplayInfo()
            },
            onCancel = {
                //                playButton.isActivated = false
            },
            onRepeat = {
                //                animationView.performanceTracker?.logRenderTimes()
//                updateRenderTimesPerLayer()
            }
        )
        dashboard_power_switch.addAnimatorListener(animatorListener)
        dashboard_power_switch.setMinAndMaxFrame(35, 47)
        dashboard_power_switch.playAnimation()
    }

    fun onSettingsClick(@Suppress("UNUSED_PARAMETER") v: View) {
        logD("onSettingsClick")
    }

    private fun runGears() {
        logD("runGears")
        dashboard_gears.playAnimation()
        dashboard_gears_outer.playAnimation()
//        dashboard_progress_indicator.playAnimation()
//        timer(10000, 1000).start()
//        val r = Runnable {
////            sendMessage(MSG, params.id)
////            taskFinished(params, false)
//            logD("stop!!")
//            dashboard_gears.cancelAnimation()
//            dashboard_power_switch.removeAllAnimatorListeners()
//            dashboard_power_switch.setMinAndMaxFrame(1, 11)
//            dashboard_power_switch.frame = 1
//            dashboard_power_switch.playAnimation()
//        }
//        Handler().postDelayed(r, 10000)
    }

//    // Method to configure and return an instance of CountDownTimer object
//    private fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
//        return object : CountDownTimer(millisInFuture, countDownInterval) {
//            override fun onTick(millisUntilFinished: Long) {
//                logD("*")
////                counter_progress_bar.progress += 10
//            }
//
//            override fun onFinish() {
//                dashboard_gears.cancelAnimation()
//                dashboard_gears_outer.cancelAnimation()
////                dashboard_progress_indicator.cancelAnimation()
//                dashboard_power_switch.removeAllAnimatorListeners()
//                dashboard_power_switch.setMinAndMaxFrame(1, 11)
//                dashboard_power_switch.frame = 1
//                dashboard_power_switch.playAnimation()
//
//                // End
//                dashboard_display_1.apply {
//                    text = "SCAN COMPLETED"
//                    gravity = Gravity.CENTER
//                    // IF match, green, else, ...
//                    setTextColor(resources.getColor(R.color.bright_2))
//                }
////                dashboard_display_2.text = "DURATION: 10:45sec"
//                dashboard_display_2.apply {
//                    text = "DURATION: 10:45sec"
//                    gravity = Gravity.CENTER
//                }
//                dashboard_display_3.text = ""
////                dashboard_display_4.text =
//                dashboard_display_4.apply {
//                    text = "CHECK DESTINATION FOLDER"
//                    gravity = Gravity.CENTER
////                    textSize = 16f
//                }
//                dashboard_display_5.text = ""
//            }
//        }
//    }

    companion object {
        internal const val REQUEST_CODE_WRITE_EXT_STORAGE = 1
    }
}
