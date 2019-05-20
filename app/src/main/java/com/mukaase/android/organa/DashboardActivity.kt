package com.mukaase.android.organa

//import android.text.Html.FROM_HTML_MODE_LEGACY
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.content_main.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val vmFactory = InjectorUtils.provideConsoleViewModel(this)
        viewModel = ViewModelProviders.of(this, vmFactory).get(DashboardViewModel::class.java)
        initObservers()

//        dashboard_display_layout.children.iterator().

//        viewLifecycleOwner.lifecycleScope.launch {
//            val params = TextViewCompat.getTextMetricsParams(textView)
//            val precomputedText = withContext(Dispatchers.Default) {
//                PrecomputedTextCompat.create(longTextContent, params)
//            }
//            TextViewCompat.setPrecomputedText(textView, precomputedText)
//        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.checkStoragePermissions(this)
    }

    private fun initObservers() {
        println("initObservers")

        viewModel.srcDirName.observe(this, Observer {
            dashboard_src_panel_name.text = it
            dashboard_display_1.text = getString(R.string.system_check)
            dashboard_display_2.text = getString(R.string.src_ok)
        })
        viewModel.srcDirPath.observe(this, Observer {
            dashboard_src_panel_path.text = it
        })
        // 49MB or 89 audio files
        viewModel.srcDirAudioFileCount.observe(this, Observer {
            dashboard_src_panel_file_count.text = it.toString()
        })

        viewModel.destDirName.observe(this, Observer {
            dashboard_dest_panel_name.text = it
            dashboard_display_3.text = getString(R.string.dest_ok)
            dashboard_display_5.text = getString(R.string.power_up_to_start)
        })
        viewModel.destDirPath.observe(this, Observer {
            dashboard_dest_panel_path.text = it
        })
        viewModel.destDirAvSpace.observe(this, Observer {
            dashboard_dest_panel_file_count.text = it
        })


        viewModel.engineStats.observe(this, Observer {
            counter_cleaned.text = it.cleaned.toString()
            counter_skipped.text = it.skipped.toString()
            counter_mp3.text = it.progress.toString()
            counter_remaining.text = getString(R.string.precision_1, it.progress, "%")

            dashboard_display_1.text = getString(R.string.scanning_, it.audioMetadata.fileName)
            dashboard_display_2.text = getString(R.string.title_, it.audioMetadata.title)
            dashboard_display_3.text = getString(R.string.artist_, it.audioMetadata.artist)
            dashboard_display_4.text = getString(R.string.album_, it.audioMetadata.album)
            dashboard_display_5.text = getString(R.string.scanning_, it.audioMetadata.fileName)

            println("monitoring the progress: ${it.progress}")

            if (it.progress == 100.0f){
                dashboard_gears.cancelAnimation()
                dashboard_gears_outer.cancelAnimation()
//                dashboard_progress_indicator.cancelAnimation()
                dashboard_power_switch.removeAllAnimatorListeners()
                dashboard_power_switch.setMinAndMaxFrame(1, 11)
                dashboard_power_switch.frame = 1
//                dashboard_power_switch.playAnimation()
                counter_remaining.text = "100%"


                dashboard_display_1.text = getString(R.string.scan_complete)
                dashboard_display_2.text = formatHtmlString(R.string.title_, it.audioMetadata.title)
                dashboard_display_3.text = getString(R.string.artist_, it.audioMetadata.artist)
                dashboard_display_4.text = getString(R.string.album_, it.audioMetadata.album)
                dashboard_display_5.text = getString(R.string.scanning_, it.audioMetadata.fileName)
            }
        })

        viewModel.timeElapsed.observe(this, Observer {
            counter_duration.text = it
        })
    }

    override fun onDestroy() {
        println("onDestroy")
        super.onDestroy()
    }

    private fun initViews() {
        println("initViews")

        //TODO: Do within a coroutine
        dashboard_power_switch.frame = 35

        counter_remaining_label.isSelected = true
        counter_cleaned_label.isSelected = true
        counter_skipped_label.isSelected = true
        counter_duration_label.isSelected = true
        dashboard_src_panel_name.isSelected = true
//        dashboard_src_panel_path.isSelected = true
        dashboard_dest_panel_name.isSelected = true
//        dashboard_dest_panel_path.isSelected = true

        println("wan2: ${Environment.getExternalStorageDirectory()}")
        getExternalFilesDirs(null).forEach({
            println("it: $it")
        })
        println("two: ${filesDir}")
//        2019-05-15 20:54:20.108 14345-14345/com.mukaase.android.organa I/System.out: wan2: /storage/emulated/0
//        2019-05-15 20:54:20.114 14345-14345/com.mukaase.android.organa I/System.out: wan: [Ljava.io.File;@90b0e5
//        2019-05-15 20:54:20.114 14345-14345/com.mukaase.android.organa I/System.out: two: /data/user/0/com.mukaase.android.organa/files
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        viewModel.onActivityResult(requestCode, resultCode, resultData)
    }

    fun onSourceDirBtnClick(v: View) {
        println("onSourceDirBtnClick")
        viewModel.openSourceDirectory(this)
    }

    fun onDestDirBtnClick(v: View) {
        println("onDestDirBtnClick")
        viewModel.openDestDirectory(this)
    }

    fun onPowerSwitch(v: View) {
        println("onPowerSwitch")
        val animatorListener = AnimatorListenerAdapter(
            onStart = { },
            onEnd = {
                //                playButton.isActivated = false
//                animationView.performanceTracker?.logRenderTimes()
//                updateRenderTimesPerLayer()
//                runGears()
                println("wee dunn!!!")
                viewModel.start(this)
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

    fun onSettingsClick(v: View) {
        println("onSettingsClick")
    }

    private fun updateDisplayInfo() {
        // Progress
        dashboard_display_1.apply {
            text = "[SCANNING]: AUD-WA0001231238.mp3"
            gravity = Gravity.START
            setTextColor(resources.getColor(R.color.bright_2))
        }
        dashboard_display_2.apply {
            text = "TITLE: KPO"
            gravity = Gravity.START
            setTextColor(resources.getColor(R.color.orange))
        }
        dashboard_display_3.apply {
            text = "ALBULM: KPAY"
            gravity = Gravity.START
            textSize = 14f
            setTextColor(resources.getColor(R.color.orange))
        }
        dashboard_display_4.apply {
            text = "AUTHOR: KPA"
            gravity = Gravity.START
            setTextColor(resources.getColor(R.color.orange))
        }
        dashboard_display_5.apply {
            text = "STATUS: MATCH | SKIPPED"
            gravity = Gravity.START
            // IF match, green, else, ...
            setTextColor(resources.getColor(R.color.orange))
        }
    }

    private fun runGears() {
        println("runGears")
        dashboard_gears.playAnimation()
        dashboard_gears_outer.playAnimation()
//        dashboard_progress_indicator.playAnimation()
//        timer(10000, 1000).start()
//        val r = Runnable {
////            sendMessage(MSG, params.id)
////            taskFinished(params, false)
//            println("stop!!")
//            dashboard_gears.cancelAnimation()
//            dashboard_power_switch.removeAllAnimatorListeners()
//            dashboard_power_switch.setMinAndMaxFrame(1, 11)
//            dashboard_power_switch.frame = 1
//            dashboard_power_switch.playAnimation()
//        }
//        Handler().postDelayed(r, 10000)
    }

    // Method to configure and return an instance of CountDownTimer object
    private fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                println("*")
//                counter_progress_bar.progress += 10
            }

            override fun onFinish() {
                dashboard_gears.cancelAnimation()
                dashboard_gears_outer.cancelAnimation()
//                dashboard_progress_indicator.cancelAnimation()
                dashboard_power_switch.removeAllAnimatorListeners()
                dashboard_power_switch.setMinAndMaxFrame(1, 11)
                dashboard_power_switch.frame = 1
                dashboard_power_switch.playAnimation()

                // End
                dashboard_display_1.apply {
                    text = "SCAN COMPLETED"
                    gravity = Gravity.CENTER
                    // IF match, green, else, ...
                    setTextColor(resources.getColor(R.color.bright_2))
                }
//                dashboard_display_2.text = "DURATION: 10:45sec"
                dashboard_display_2.apply {
                    text = "DURATION: 10:45sec"
                    gravity = Gravity.CENTER
                }
                dashboard_display_3.text = ""
//                dashboard_display_4.text =
                dashboard_display_4.apply {
                    text = "CHECK DESTINATION FOLDER"
                    gravity = Gravity.CENTER
//                    textSize = 16f
                }
                dashboard_display_5.text = ""
            }
        }
    }

    private fun things() {
        //        dashboard_display_2.text = "TITLE: KPO"
//        dashboard_display_3.text = "ALBULM: KPAY"
//        dashboard_display_4.text = "AUTHOR: KPA"
//        dashboard_display_5.text = "STATUS: MATCH | SKIPPED"
//        dashboard_display_6.text = true

//        // Error
//        dashboard_display_1.text = "ALERT!!!"
//        dashboard_display_2.text = ""
//        dashboard_display_3.text = "DESTINATION FULL"
//        dashboard_display_4.text = ""
//        dashboard_display_5.text = ""

    }
}
