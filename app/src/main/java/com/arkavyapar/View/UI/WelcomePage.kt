package com.arkavyapar.View.UI

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.arkavyapar.Constant.Animation
import com.arkavyapar.R
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils

class WelcomePage : AppCompatActivity() {
    lateinit var mActivity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)
        iniView()
        trunOnGPS()
        Animation.setAnimation(mActivity)
    }
    private fun iniView() {
        mActivity = this@WelcomePage
    }
    fun clickToAgreeForward(view: View) {
        Utils.launchActivity(mActivity,SignUP_1::class.java)}

    fun clickOnTermsConditions(view: View) {
        ToastUtils.shortToast("URL Not Found")
    }
    private fun trunOnGPS() {
        val provider: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)

        if (!provider.contains("gps")) { //if gps is disabled
            val poke = Intent()
            poke.setClassName(
                "com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider"
            )
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            sendBroadcast(poke)
        }
    }

}