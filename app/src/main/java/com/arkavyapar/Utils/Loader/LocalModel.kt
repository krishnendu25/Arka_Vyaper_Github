package com.arkavyapar.Utils.Loader

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager.BadTokenException
import com.arkavyapar.R


class LocalModel {
    /**
     * Hold current activity.
     */
    var currentActivity: Activity? = null
        private set

    /**
     * Hold current context.
     */
    var context: Activity? = null
        set(context) {
            field = context
            currentActivity = if (context is Activity) {
                context
            } else {
                null
            }
        }
    private var progressDialog: AlertDialog ? = null
    fun showProgressDialog(context: Activity?, msg: String?) {
        try {
            try {
                try {
                    if (progressDialog != null) {
                        if (!context!!.isFinishing()) {
                            progressDialog!!.show()
                        }
                    } else {
                        val dialogBuilder = AlertDialog.Builder(context)
                        val inflater: LayoutInflater = context!!.getLayoutInflater()
                        val dialogView: View = inflater.inflate(
                            R.layout.progress_dialog_layout,
                            null
                        )
                        dialogBuilder.setView(dialogView)
                        dialogBuilder.setCancelable(true)
                        progressDialog = dialogBuilder.create()
                        progressDialog!!.getWindow()!!.setBackgroundDrawableResource(R.color.transparent)
                        progressDialog!!.show()

                    }
                } catch (e: BadTokenException) {
                    //use a log message
                }
            } catch (E: IllegalStateException) {
            }
        } catch (e: java.lang.Exception) {
        }
    }

    fun cancelProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog!!.hide()
                progressDialog = null
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        /**
         * Instance of this class.
         */
        private var localModel: LocalModel? = null

        /**
         * Returns the singleton instance of this class.
         *
         * @return
         */
        val instance: LocalModel?
            get() {
                if (localModel == null) {
                    localModel = LocalModel()
                }
                return localModel
            }
    }
}