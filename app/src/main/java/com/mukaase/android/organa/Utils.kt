package com.mukaase.android.organa

import android.content.Context
import android.text.Spanned
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat


fun Context.formatHtmlString(@StringRes resId: Int, vararg formatArgs: Any): Spanned {
    return HtmlCompat.fromHtml(this.getString(resId, formatArgs), HtmlCompat.FROM_HTML_MODE_LEGACY)
}
