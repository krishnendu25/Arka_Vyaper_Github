package com.arkavyapar.View.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.Utils.Prefs
import com.arkavyapar.Utils.StringUtils.obj.loginStatus
import com.arkavyapar.R
import com.arkavyapar.Utils.Utils


class Splash_Screen : AppCompatActivity() {
    var mPrefs:Prefs?=null
    var mContext:Context?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniView()
        setContentView(R.layout.activity_splash__screen)
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }
        val handler = Handler()
        val runnable = Runnable {
            if (mPrefs!!.getBoolean(loginStatus, false)) {
                Utils.launchActivityWithFinish(this@Splash_Screen, DashBoardActivity::class.java)
            } else {
                Utils.launchActivityWithFinish(this@Splash_Screen, LoginActivity::class.java)
            }
        }
        handler.postDelayed(runnable, 2100)

    }

    private fun iniView() {
        mContext=applicationContext
        mPrefs= Prefs(mContext!!)
    }

}