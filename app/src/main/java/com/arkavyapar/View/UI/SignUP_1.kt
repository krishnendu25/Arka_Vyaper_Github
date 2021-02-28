package com.arkavyapar.View.UI

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.AppConstants
import com.arkavyapar.Constant.Constants
import com.arkavyapar.R
import com.arkavyapar.Utils.Permissons
import com.arkavyapar.Utils.PhotoPicker.ImagePickerActivity
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONObject
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.arkavyapar.App
import com.arkavyapar.Utils.Loader.LocalModel
import java.io.ByteArrayOutputStream;

class SignUP_1 : AppCompatActivity() {
    private var bitmap: Bitmap?  = null
    var fullNameED: EditText? = null
    var mobileED: EditText? = null
    var emailED: EditText? = null
    var passwordED: EditText? = null
    var profileIV: CircleImageView?=null
    lateinit var mActivity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_1)
        iniView()
        Animation.setAnimation(mActivity)
    }

    private fun iniView() {
        fullNameED =  findViewById(R.id.fullNameED);
        mobileED =  findViewById(R.id.mobileED);
        emailED =  findViewById(R.id.emailED);
        passwordED =  findViewById(R.id.passwordED);
        profileIV =  findViewById(R.id.profileIV);
        mActivity = this@SignUP_1
    }

    fun selectProfileDP(view: View) {
        selectPicture(1)
    }

    private fun selectPicture(max: Int) {

        if (Permissons.isAllPermissionsGranted(mActivity)){
            try {
                var intent = Intent(mActivity, ImagePickerActivity::class.java)
                intent.putExtra("Select_Max", max.toString())
                startActivityForResult(intent, AppConstants.PROFILE_PICTURE)

            } catch (e: Exception) {
                ToastUtils.shortToast("ImagePicker Error")
            }
        }else{
            Permissons.requestAllPermissions(mActivity)
        }


    }

    fun hitsignUpDone(view: View) {
        LocalModel.instance!!.showProgressDialog(this,"")
        if (validation()){
            var jsonObject = JSONObject()
            jsonObject.put(StringUtils.go_fullName,fullNameED!!.text.toString())
            jsonObject.put(StringUtils.go_phoneNo,mobileED!!.text.toString())
            jsonObject.put(StringUtils.go_emailID,emailED!!.text.toString())
            jsonObject.put(StringUtils.go_passWord,passwordED!!.text.toString())
            jsonObject.put(StringUtils.profilePicture,encodeImage(bitmap!!))
            LocalModel.instance!!.cancelProgressDialog()
            App.instance!!.mPrefs!!.setString(StringUtils.signUpBundel,jsonObject.toString())
            Utils.launchActivity(mActivity,MobileNoVerification::class.java)
        }else{
            LocalModel.instance!!.cancelProgressDialog()
        }
    }

    private fun validation(): Boolean {
        if (fullNameED!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your Full Name")
            Animation.editText_Sh(fullNameED!!)
            return false
        } else if (mobileED!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your Mobile No")
            Animation.editText_Sh(mobileED!!)
            return false
        } else if (emailED!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your EmailID")
            Animation.editText_Sh(emailED!!)
            return false
        }else if (passwordED!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Your Password")
            Animation.editText_Sh(passwordED!!)
            return false
        }else if (mobileED!!.text.length!=10){
            ToastUtils.shortToast("Please Enter A Valid  Phone No")
            Animation.editText_Sh(mobileED!!)
            return false
        }else if (Constants.isMobileValid(mobileED!!.text.toString())){
            ToastUtils.shortToast("Please Enter A Valid  Phone No")
            Animation.editText_Sh(passwordED!!)
            return false
        } else if (!Constants.isValidEmail(emailED!!.text.toString())){
            ToastUtils.shortToast("Please Enter A Valid  EmailID")
            Animation.editText_Sh(emailED!!)
            return false
        } else if (bitmap==null){
            ToastUtils.shortToast("Please Select A Profile Picture")
            Animation.editText_Sh(profileIV!!)
            return false
        }
        else {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== RESULT_OK) {
            when (requestCode) {
                AppConstants.PROFILE_PICTURE -> {
                    var image_uris: ArrayList<Uri> = data!!.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS)
                    val bmOptions = BitmapFactory.Options()
                     bitmap= BitmapFactory.decodeFile(image_uris[0].path, bmOptions)
                     profileIV!!.setImageBitmap(bitmap)
                }
            }
        }
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}