package com.arkavyapar.View.UI

import `in`.aabhasjindal.otptextview.OtpTextView
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Model.ReponseModel.SendOTP
import com.arkavyapar.Model.ReponseModel.VerifyOTP
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import com.swiftsynq.otpcustomview.CustomOtpView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MobileNoVerification : AppCompatActivity() {
    private var MyphoneNo: String? = null
    var otpView: OtpTextView? = null
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
        MyphoneNo=JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel,"")).getString(StringUtils.go_phoneNo);
        showPhoneNumber!!.setText("sent to "+MyphoneNo)
        hitsendOTP(MyphoneNo!!,false)
    }

    private fun hitsendOTP(phoneNo: String,resend:Boolean) {
        LocalModel.instance!!.showProgressDialog(mActivity, "Loading..")
        val requestCall: Call<SendOTP> = App.instance!!.apiInterface!!.Sendotp(phoneNo)
        requestCall.enqueue(object : Callback<SendOTP> {
            override fun onResponse(call: Call<SendOTP>, response: Response<SendOTP>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")){
                        if (resend)
                            ToastUtils.shortToast("OTP send successfully")
                        else
                            ToastUtils.shortToast("OTP resend successfully")
                    }
                } else {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }
            override fun onFailure(call: Call<SendOTP>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })
    }

    fun verifyContinueNext(view: View) {

        if (otpView!!.getOTP().length==4){
            LocalModel.instance!!.showProgressDialog(mActivity, "Loading..")
            val requestCall: Call<VerifyOTP> = App.instance!!.apiInterface!!.Verifyotp(MyphoneNo,otpView!!.otp)
            requestCall.enqueue(object : Callback<VerifyOTP> {
                override fun onResponse(call: Call<VerifyOTP>, response: Response<VerifyOTP>) {
                    if (response.body() != null) {
                        LocalModel.instance!!.cancelProgressDialog()
                        if (response.body()?.success.toString().trim().equals("1")){
                            Utils.launchActivity(mActivity,ChooseCustomerType::class.java)
                        }
                    } else {
                        ToastUtils.shortToast(response.body()?.msg.toString())
                        LocalModel.instance!!.cancelProgressDialog()
                    }
                }
                override fun onFailure(call: Call<VerifyOTP>, t: Throwable) {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            })
        }else{
            ToastUtils.shortToast("Please Enter OTP")
        }


    }

    fun hitResendOTP(view: View) {
        otpView!!.otp=""
        hitsendOTP(MyphoneNo!!,true)
    }
}