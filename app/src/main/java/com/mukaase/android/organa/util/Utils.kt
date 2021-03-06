package com.mukaase.android.organa.util

import android.app.Activity
import android.content.Context
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.StringRes
import androidx.annotation.IntegerRes
import androidx.annotation.ColorRes
import androidx.core.text.HtmlCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.mukaase.android.organa.console.ConsoleViewModel


fun Context.formatHtmlString(@StringRes resId: Int, vararg formatArgs: Any): Spanned {
    return HtmlCompat.fromHtml(this.getString(resId, formatArgs), HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun Any.logD(message: String){
  // if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
  // Log.d(this.getString(R.string.app_name), "${this::class.java.simpleName} $message")
  Log.d("Organa", "${this::class.java.simpleName} $message")
}

fun Any.logE(message: String) = Log.e("Organa", message)

fun Any.logW(message: String) = Log.w("Organa", message)
fun Any.logI(message: String) = Log.i("Organa", message)

// Debug - method + params
// Info - system / user initiated call
// Warn - could become an error

fun AppCompatActivity.toast(message: String){
  Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
}

fun Context.colour(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

//fun Activity.viewModel() {
//    ViewModelProviders.of(this, vmFactory).get(ConsoleViewModel::class.java)
//}