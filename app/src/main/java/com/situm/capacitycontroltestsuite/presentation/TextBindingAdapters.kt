package com.situm.capacitycontroltestsuite.presentation

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("htmlStringBuilder")
fun setHtmlStringBuilder(tv: TextView, sb: StringBuilder?) {
    tv.text = sb?.toString()?.fromHTML() ?: ""
    tv.scrollTo(0, (tv.lineCount * tv.lineHeight) - (tv.bottom - tv.top))
}

fun String.fromHTML(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}