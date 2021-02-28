package com.arkavyapar.View.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import org.json.JSONObject


class ChooseCustomerType : AppCompatActivity() {
    var sellerRB: RadioButton? = null
    var buyerRB: RadioButton? = null
    var nextTV: TextView? = null
    var customerType:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_customer_type)
        iniView()
        Animation.setAnimation(this@ChooseCustomerType)
        CustomerTypeSelected(true)

    }

    private fun iniView() {
        sellerRB =  findViewById(R.id.sellerRB);
        buyerRB =  findViewById(R.id.buyerRB);
        nextTV =  findViewById(R.id.nextTV);
    }

    fun nextPageFromRegister(view: View) {
        LocalModel.instance!!.showProgressDialog(this,"")
        //Add UserType With Old Data
        App.instance!!.mPrefs!!.setString(StringUtils.signUpBundel,
            JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel, "")).put(StringUtils.userRole,customerType)
                .toString())
        LocalModel.instance!!.cancelProgressDialog()
        val intent= Intent(this, GetMyLocation::class.java)
        startActivity(intent)
    }

    fun CustomerTypeSelected(isBuyer:Boolean){
        if (isBuyer){
            buyerRB!!.setChecked(true)
            sellerRB!!.setChecked(false)
            customerType = StringUtils.I_am_Buyer
        }else{
            sellerRB!!.setChecked(true)
            buyerRB!!.setChecked(false)
            customerType = StringUtils.I_am_Seller
        }
    }

    fun iAMSeller(view: View) { CustomerTypeSelected(false)}
    fun iAMVByer(view: View) {CustomerTypeSelected(true)}

}