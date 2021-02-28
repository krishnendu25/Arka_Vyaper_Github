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
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.Permissons
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import org.json.JSONObject


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
            LocalModel.instance!!.showProgressDialog(this,"")
            if (validation()) {
                LocalModel.instance!!.cancelProgressDialog()
                try {
                    if (!App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel, "").equals(""))
                    {
                        var userDetails =
                            JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel, ""))
                        if (userDetails.getString(StringUtils.go_phoneNo)
                                .equals(userIdED!!.text.toString().trim())
                            && userDetails.getString(StringUtils.go_passWord).equals(
                                passwordED!!.text.toString().trim()
                            )
                        ) {
                            App.instance!!.mPrefs!!.setBoolean(StringUtils.loginStatus, true)
                            Utils.launchActivityWithFinish(mActivity, DashBoardActivity::class.java)
                        }else{
                            ToastUtils.shortToast("Please Enter Your  Valid Credential")
                            Animation.editText_Sh(root!!)
                        }
                    }else{
                        Animation.editText_Sh(root!!)
                        ToastUtils.shortToast("You Have No Account please Sign up")
                    }
                } catch (e: Exception) {
                    Animation.editText_Sh(root!!)
                    ToastUtils.shortToast("You Have No Account please Sign up")
                }
            }else{
                LocalModel.instance!!.cancelProgressDialog()
            }
        }else{
            trunOnGPS()
        }
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