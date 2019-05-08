package com.mukaase.android.organa

import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.children

import kotlinx.android.synthetic.main.activity_main.*
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
                counter_progress_bar.progress += 10
            }

            override fun onFinish() {
                dashboard_gears.cancelAnimation()
                dashboard_gears_outer.cancelAnimation()
//                dashboard_progress_indicator.cancelAnimation()
                dashboard_power_switch.removeAllAnimatorListeners()
                dashboard_power_switch.setMinAndMaxFrame(1, 11)
                dashboard_power_switch.frame = 1
                dashboard_power_switch.playAnimation()
            }
        }
    }

//    private fun stopGears(b: Boolean) {
//        dashboard_gears.cancelAnimation()
//    }


}
