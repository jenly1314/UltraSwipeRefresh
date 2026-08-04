package com.king.ultraswiperefresh.app.ext

import android.content.Context
import android.widget.Toast

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */

var toast: Toast? = null

fun Context.showToast(text: String) {
    toast?.cancel()
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    toast?.show()
}