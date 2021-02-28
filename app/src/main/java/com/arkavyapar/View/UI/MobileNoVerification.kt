package com.arkavyapar.View.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.R
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.Utils
import com.swiftsynq.otpcustomview.CustomOtpView
import org.json.JSONObject


class MobileNoVerification : AppCompatActivity() {
    var otpView: CustomOtpView? = null
    lateinit var mActivity: Activity
    var showPhoneNumber: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_no_verification)
        iniView()
        Animation.setAnimation(mActivity)
    }

    private fun iniView() {
        otpView =  findViewById(R.id.otpView);
        showPhoneNumber =  findViewById(R.id.showPhoneNumber);
        mActivity = this@MobileNoVerification
        showPhoneNumber!!.setText("sent to "+JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel,"")).getString(StringUtils.go_phoneNo))
    }

    fun verifyContinueNext(view: View) {
        Utils.launchActivity(mActivity,ChooseCustomerType::class.java)
    }
    fun hitResendOTP(view: View) {}
}