package com.arkavyapar.View.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Model.ReponseModel.LoginModel
import com.arkavyapar.Model.ReponseModel.VerifyOTP
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.Permissons
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var userIdED: EditText? = null
    var passwordED: EditText? = null
    var root: RelativeLayout? = null
    lateinit var mActivity: Activity
    private var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        iniView()
        Animation.setAnimation(mActivity)
        Permissons.requestAllPermissions(mActivity)
    }

    private fun iniView() {
        userIdED = findViewById(R.id.userIdED)
        passwordED = findViewById(R.id.passwordED)
        root = findViewById(R.id.root)
        mActivity = this@LoginActivity
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
    }

    fun doLoginWork(view: View) {
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (validation()) {
            hitLogin()

            }else{
                LocalModel.instance!!.cancelProgressDialog()
            }
        }else{
            trunOnGPS()
        }
    }

    private fun hitLogin() {
        LocalModel.instance!!.showProgressDialog(mActivity, "Loading..")
        val requestCall: Call<LoginModel> = App.instance!!.apiInterface!!.Login(userIdED!!.text.toString(),passwordED!!.text.toString(),"16500215025")
        requestCall.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")){
                        App.instance!!.mPrefs!!.setString(StringUtils.userID, response.body()?.uid!!)
                        App.instance!!.mPrefs!!.setBoolean(StringUtils.loginStatus, true)

                        if (response.body()?.usertype.toString().trim().equals("1")) {
                            App!!.instance!!.mPrefs!!.setString(StringUtils.userRole,StringUtils.I_am_Buyer)
                        } else {
                            App!!.instance!!.mPrefs!!.setString(StringUtils.userRole,StringUtils.I_am_Seller)
                        }


                        Utils.launchActivityWithFinish(mActivity, DashBoardActivity::class.java)
                    } else {
                        ToastUtils.shortToast("Login Unsuccessful")
                        LocalModel.instance!!.cancelProgressDialog()
                    }
                } else {
                    ToastUtils.shortToast("Login Unsuccessful")
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
                ToastUtils.shortToast("Login Unsuccessful")
            }
        })
    }

    private fun trunOnGPS() {
        ToastUtils.longToast("PLEASE TURN ON GPS IN HIGH ACCURACY")
    }

    private fun validation(): Boolean {
        if (userIdED!!.text.isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Your UserName")
            Animation.editText_Sh(userIdED!!)
            return false

        } else if (passwordED!!.text.isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Your Password")
            Animation.editText_Sh(passwordED!!)
            return false
        } else {
            return true
        }
    }

    fun gotoRegestionPage(view: View) {
        Utils.launchActivity(mActivity, WelcomePage::class.java)
    }
}

