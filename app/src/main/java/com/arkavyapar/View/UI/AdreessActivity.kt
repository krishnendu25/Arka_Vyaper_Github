package com.arkavyapar.View.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.Constants
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class AdreessActivity : AppCompatActivity() {
    var searchsrcED: EditText? = null
    var address1Tv: EditText? = null
    var address2Tv: EditText? = null
    var pincodeTV: EditText? = null
    var cityTV: EditText? = null
    var StateTV: EditText? = null
    var nextAddress: TextView? = null
    var allAdressDtails:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adreess)
        iniView()
        Animation.setAnimation(this@AdreessActivity)
        try{
            allAdressDtails = intent.getStringExtra(StringUtils.intent_Address)
            var details = JSONObject(allAdressDtails)
            address1Tv!!.setText(details.getString("address"))
            address2Tv!!.setText(details.getString("address"))
            pincodeTV!!.setText(details.getString("postalCode"))
            cityTV!!.setText(details.getString("city"))
            StateTV!!.setText(details.getString("state"))
        }catch (E:Exception){

        }
    }
    private fun iniView() {
        searchsrcED = findViewById(R.id.searchsrcED)
        address1Tv = findViewById(R.id.address1Tv)
        address2Tv = findViewById(R.id.address2Tv)
        pincodeTV = findViewById(R.id.pincodeTV)
        cityTV = findViewById(R.id.cityTV)
        StateTV = findViewById(R.id.StateTV)
        nextAddress = findViewById(R.id.nextAddress)
    }

    fun nextPageFromMyAddress(view: View) {
        LocalModel.instance!!.showProgressDialog(this,"")
        if (validation()){
            var jsonObject = JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel,""))
            jsonObject.put(StringUtils.go_address_1,address1Tv!!.text.toString())
            jsonObject.put(StringUtils.go_address_2,address2Tv!!.text.toString())
            jsonObject.put(StringUtils.go_Pincode,pincodeTV!!.text.toString())
            jsonObject.put(StringUtils.go_city,cityTV!!.text.toString())
            jsonObject.put(StringUtils.go_state,StateTV!!.text.toString())
            App.instance!!.mPrefs!!.setString(StringUtils.signUpBundel,jsonObject.toString())
            LocalModel.instance!!.cancelProgressDialog()
            Utils.launchActivity(this,IdentityVerificationActivity::class.java)
        }else{
            LocalModel.instance!!.cancelProgressDialog()
        }
    }

    private fun validation(): Boolean {
        if (address1Tv!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Address 1")
            Animation.editText_Sh(address1Tv!!)
            return false
        }else if (address2Tv!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Address 2")
            Animation.editText_Sh(address2Tv!!)
            return false
        }else if (pincodeTV!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your PinCode")
            Animation.editText_Sh(pincodeTV!!)
            return false
        }else if (cityTV!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your City")
            Animation.editText_Sh(cityTV!!)
            return false
        }else if (StateTV!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your State")
            Animation.editText_Sh(StateTV!!)
            return false
        }
        else {
            return true
        }
    }
}