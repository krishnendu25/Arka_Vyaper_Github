package com.arkavyapar.View.UI.Fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Model.ReponseModel.CommonModel
import com.arkavyapar.Model.ReponseModel.UserDetails
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import com.arkavyapar.View.UI.DashBoardActivity
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    var profileIV: CircleImageView? = null
    var fullNameED: EditText? = null
    var mobileED: EditText? = null
    var emailED: EditText? = null
    var catagoryDD: TextView? = null
    var address1Tv: EditText? = null
    var address2Tv: EditText? = null
    var pincodeTV: EditText? = null
    var cityTV: EditText? = null
    var StateTV: EditText? = null
    var saveProfileDetails: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        iniview(view)
        catagoryDD!!.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                PopupMenu(v!!.context, v).apply {
                    menuInflater.inflate(R.menu.role_menu, menu)
                    setOnMenuItemClickListener { item ->
                        catagoryDD!!.setText(item.title)
                        true
                    }
                }.show()
            }

        })
        saveProfileDetails!!.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(validation()) {

                    hitUpdate(fullNameED!!.text.toString().toString(),catagoryDD!!.text.toString(),address1Tv!!.text.toString(),address2Tv!!.text.toString(),
                            pincodeTV!!.text.toString(),cityTV!!.text.toString(),StateTV!!.text.toString(),emailED!!.text.toString())
                }
            }
        })
        return view
    }

    private fun validation(): Boolean {
        if (fullNameED!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter FullName")
            Animation.editText_Sh(fullNameED!!)
            return false
        }else if (catagoryDD!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Select User Type")
            Animation.editText_Sh(catagoryDD!!)
            return false
        }else if (address1Tv!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Address")
            Animation.editText_Sh(address1Tv!!)
            return false
        }else if (address2Tv!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Address 2")
            Animation.editText_Sh(address2Tv!!)
            return false
        }else if (pincodeTV!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Pincode")
            Animation.editText_Sh(pincodeTV!!)
            return false
        }else if (cityTV!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter City")
            Animation.editText_Sh(cityTV!!)
            return false
        }else if (StateTV!!.text.toString().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter State")
            Animation.editText_Sh(StateTV!!)
            return false
        }
        else {
            return true
        }
    }

    private fun iniview(view: View?) {
        profileIV = view!!.findViewById(R.id.profileIV)
        fullNameED = view.findViewById(R.id.fullNameED)
        mobileED = view.findViewById(R.id.mobileED)
        emailED = view.findViewById(R.id.emailED)
        catagoryDD = view.findViewById(R.id.catagoryDD)
        address1Tv = view.findViewById(R.id.address1Tv)
        address2Tv = view.findViewById(R.id.address2Tv)
        pincodeTV = view.findViewById(R.id.pincodeTV)
        cityTV = view.findViewById(R.id.cityTV)
        StateTV = view.findViewById(R.id.StateTV)
        saveProfileDetails = view.findViewById(R.id.saveProfileDetails)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
            }
    }

    private fun hitgetUser(isLoderShow: Boolean, userid: String) {
        if (isLoderShow) {
            LocalModel.instance!!.showProgressDialog(activity, "Getting location....")
        }

        val requestCall: Call<UserDetails> = App.instance!!.apiInterface!!.userdetails(userid)
        requestCall.enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")) {

                        fullNameED!!.setText(response.body()?.userdetails!!.USERNAME)
                        address1Tv!!.setText(response.body()?.userdetails!!.ADDRESS1)
                        if (response.body()?.userdetails!!.USERTYPE.equals("1")) {
                            catagoryDD!!.text = StringUtils.I_am_Buyer
                        } else {
                            catagoryDD!!.text = StringUtils.I_am_Seller

                        }



                        mobileED!!.setText(response.body()?.userdetails!!.PHONENO)
                        emailED!!.setText(response.body()?.userdetails!!.EMAIL)
                        address1Tv!!.setText(response.body()?.userdetails!!.ADDRESS1)
                        address2Tv!!.setText(response.body()?.userdetails!!.ADDRESS2)
                        pincodeTV!!.setText(response.body()?.userdetails!!.PINCODE)
                        cityTV!!.setText(response.body()?.userdetails!!.CITY)
                        StateTV!!.setText(response.body()?.userdetails!!.STATE)

                        Utils.setImageFromUrl(
                            profileIV!!,
                            response.body()?.userdetails!!.PROFILEPIC, activity!!
                        )

                    }
                }
            }

            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hitgetUser(
            true,
            App.instance!!.mPrefs!!.getString(StringUtils.userID, "").toString()
        )
    }


    private fun hitUpdate(
        fullNameED: String,
        catagoryDD: String,
        address1Tv: String,
        address2Tv: String,
        pincodeTV: String,
        cityTV: String,
        StateTV: String,
        email: String
    ) {
        LocalModel.instance!!.showProgressDialog(activity,"")
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
if (catagoryDD.equals(StringUtils.I_am_Buyer)){
    builder.addFormDataPart("userType", "1") 
}else{
    builder.addFormDataPart("userType", "2")
}
        
        builder.addFormDataPart("address_1",address1Tv)
        builder.addFormDataPart("address_2",address2Tv)
        builder.addFormDataPart("pincode", pincodeTV)
        builder.addFormDataPart("city", cityTV)
        builder.addFormDataPart("state", StateTV)
        builder.addFormDataPart("fcmToken", App.instance!!.tokenFCM)
        builder.addFormDataPart("userID", App.instance!!.mPrefs!!.getString(
            StringUtils.userID,
            ""
        ).toString())
        builder.addFormDataPart("name", fullNameED)
        builder.addFormDataPart("email", email)
    
        
        val requestCall: Call<CommonModel> = App.instance!!.apiInterface!!.hitupdateuserApi(
            builder.build())
        requestCall.enqueue(object : Callback<CommonModel> {
            override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body().success.equals("1")){
                        Utils.launchActivityWithFinish(activity!!, DashBoardActivity::class.java)
                    }
                }else{
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<CommonModel>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })
    }


}