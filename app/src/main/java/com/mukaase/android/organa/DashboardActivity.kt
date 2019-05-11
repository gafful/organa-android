package com.mukaase.android.organa

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        dashboard_power_switch.frame = 35

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

        counter_remaining_label.isSelected = true
        counter_cleaned_label.isSelected = true
        counter_duration_label.isSelected = true
        dashboard_src_panel_name.isSelected = true
        dashboard_src_panel_path.isSelected = true
        dashboard_dest_panel_name.isSelected = true
        dashboard_dest_panel_path.isSelected = true

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

    fun onSettingsClick(v: View){
        println("onSettingsClick")


    }

    fun onPowerSwitch(v: View){
        println("onPowerSwitch")
        val animatorListener = AnimatorListenerAdapter(
            onStart = {  },
            onEnd = {
//                playButton.isActivated = false
//                animationView.performanceTracker?.logRenderTimes()
//                updateRenderTimesPerLayer()
//                runGears()
                println("wee dunn!!!")
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
        timer(10000, 1000).start()
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
    private fun timer(millisInFuture:Long, countDownInterval:Long): CountDownTimer {
        return object: CountDownTimer(millisInFuture, countDownInterval){
            override fun onTick(millisUntilFinished: Long){
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
