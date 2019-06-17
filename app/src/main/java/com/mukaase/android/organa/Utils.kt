package com.mukaase.android.organa

import android.content.Context
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.StringRes
import androidx.annotation.IntegerRes
import androidx.core.text.HtmlCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat


fun Context.formatHtmlString(@StringRes resId: Int, vararg formatArgs: Any): Spanned {
    return HtmlCompat.fromHtml(this.getString(resId, formatArgs), HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun Any.logd(message: String){
  // if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
  // Log.d(this.getString(R.string.app_name), "${this::class.java.simpleName} $message")
  Log.d("Organa", "${this::class.java.simpleName} $message")
}

fun Any.logE(message: String) {
  Log.e("Organa", message)
}

// Debug - method + params
// Info - system / user initiated call
// Warn - could become an error

fun AppCompatActivity.toast(message: String){
  Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
}

fun Context.colour(@IntegerRes resId: Int) = ContextCompat.getColor(this, resId)