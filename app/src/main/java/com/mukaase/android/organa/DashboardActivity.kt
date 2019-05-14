package com.mukaase.android.organa

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
//        setSupportActionBar(toolbar)

        initViews()

        val vmFactory = InjectorUtils.provideConsoleViewModel(this)
        viewModel = ViewModelProviders.of(this, vmFactory).get(DashboardViewModel::class.java)
        viewModel.checkStoragePermissions(this)

        viewModel.srcDirPath.observe(this, Observer {
            dashboard_src_panel_path.text = it
        })

        viewModel.destDirPath.observe(this, Observer {
            dashboard_dest_panel_path.text = it
        })

//        viewModel.getUsers().observe(this, Observer<List<User>>{ users ->
//            // update UI
//        })

        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    REQUEST_CODE_WRITE_EXT_STORAGE)
//
//                // REQUEST_CODE_WRITE_EXT_STORAGE is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//            // Extra check for isReadable ...
//            println("Permission already granted")
//            val ok = File("/storage/6362-6134/01awaz").mkdir()
//            println("root-ok1: ${ok}")
//            val ok1 = File("/storage/emulated/1/01awaz").mkdir()
//            println("root-ok11: ${ok1}")
//        }




//        val drawable = BitmapDrawable(resources, dashboard_active_bar_btm.drawable.toBitmap())
//        drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
//        mRelativeLayout.setBackground(drawable);


//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)

//        // Start Up
//        dashboard_display_1.text = resources.getString(R.string.system_check)
//        dashboard_display_1.setTextColor(resources.getColor(R.color.bright))
//        dashboard_display_1.gravity = Gravity.CENTER
//        dashboard_display_2.visibility = View.GONE
//        dashboard_display_3.visibility = View.GONE
//        dashboard_display_4.visibility = View.GONE
//        dashboard_display_5.visibility = View.GONE
//        dashboard_display_6.visibility = View.GONE

//        dashboard_display_layout.children.iterator().



//        // marquee
//        dashboard_display_1.isSelected = true
//        dashboard_display_2.isSelected = true
//        dashboard_display_3.isSelected = true
//        dashboard_display_4.isSelected = true
//        dashboard_display_5.isSelected = true
////        dashboard_display_6.isSelected = true


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

        // Notice
    }

    private fun initViews() {
        println("initViews")
        dashboard_power_switch.frame = 35

        counter_remaining_label.isSelected = true
        counter_cleaned_label.isSelected = true
        counter_skipped_label.isSelected = true
        counter_duration_label.isSelected = true
        dashboard_src_panel_name.isSelected = true
        dashboard_src_panel_path.isSelected = true
        dashboard_dest_panel_name.isSelected = true
        dashboard_dest_panel_path.isSelected = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            DashboardViewModel.REQUEST_CODE_WRITE_EXT_STORAGE -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    val ok = File(Environment.getExternalStorageDirectory().path + "/0awaz").mkdir()
//                    println("root-ok2: ${ok}")
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return
//            }
//            else -> {
//                // Ignore all other requests.
//            }
//        }
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when(item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    fun onSourceDirBtnClick(v: View) {
        println("onSourceDirBtnClick")
        viewModel.openSourceDirectory(this)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        viewModel.onActivityResult(requestCode, resultCode, resultData)
//        if (requestCode == REQUEST_CODE_CHOOSE_SOURCE_DIR && resultCode == Activity.RESULT_OK) {
//            resultData?.data?.also { uri ->
//                println("Uri: $uri")
//                //content://com.android.externalstorage.documents/document/6362-6134%3ADeitrick%20Haddon-Church%20on%20the%20moon%2FFolder.jpg
//                println("Uri pat: ${FileExt.resolvePath(uri)}")
//                File("${FileExt.resolvePath(uri) + "/hopeaf"}")
////                Files.createDirectory(Path())
//            }
//        }
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
                viewModel.start()
                runGears()
                updateDisplayInfo()
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

//    private fun stopGears(b: Boolean) {
//        dashboard_gears.cancelAnimation()
//    }


}
