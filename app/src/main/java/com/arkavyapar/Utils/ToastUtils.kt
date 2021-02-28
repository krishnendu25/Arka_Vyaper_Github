package com.arkavyapar.Utils

import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.arkavyapar.App


class ToastUtils {

    companion object obj{
        fun shortToast(@StringRes text: Int) {
            shortToast(App.instance!!.getString(text))
        }

        fun shortToast(text: String) {
            show(text, Toast.LENGTH_SHORT)
        }

        fun longToast(@StringRes text: Int) {
            longToast(App.instance!!.getString(text))
        }

        fun longToast(text: String) {
            show(text, Toast.LENGTH_LONG)
        }

        private fun makeToast(text: String, @ToastLength length: Int): Toast {
            return Toast.makeText(App.instance, text, length)
        }

        private fun show(text: String, @ToastLength length: Int) {
            makeToast(text, length).show()
        }

        @IntDef(Toast.LENGTH_LONG, Toast.LENGTH_SHORT)
        private annotation class ToastLength

    }
}
