package com.arkavyapar.View.UI

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.AppConstants
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.ReponseModel.CommonModel
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.Permissons
import com.arkavyapar.Utils.PhotoPicker.ImagePickerActivity
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.View.Interface.AlertTask
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class IdentityVerificationActivity : AppCompatActivity() {
    lateinit var mActivity: Activity
    private var bitmap: Bitmap? = null
    private var bitmapID: Bitmap? = null
    var profileIV: CircleImageView? = null
    var enterFullName: EditText? = null
    var attachedIDCart: TextView? = null
    var attachedIDSUCCESS: LinearLayout? = null
    var suceesFullView: CardView? = null
    var deleteSelection:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity_verification)
        Animation.setAnimation(this@IdentityVerificationActivity)
        intiView()
        setData()

    }

    private fun setData() {
        LocalModel.instance!!.showProgressDialog(mActivity,"")
        try{
            var details = JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel, ""))
            enterFullName!!.setText(details.getString(StringUtils.go_fullName))
           // profileIV!!.setImageBitmap(convertBase64ToBitmap(details.getString(StringUtils.profilePicture)))
            LocalModel.instance!!.cancelProgressDialog()
        }catch (E: java.lang.Exception){
            LocalModel.instance!!.cancelProgressDialog()
        }
    }

    private fun intiView() {
        mActivity = this@IdentityVerificationActivity
        profileIV = findViewById(R.id.profileIV)
        enterFullName = findViewById(R.id.enterFullName)
        attachedIDCart = findViewById(R.id.attachedIDCart)
        deleteSelection = findViewById(R.id.deleteSelection)
        attachedIDSUCCESS = findViewById(R.id.attachedIDSUCCESS)
        suceesFullView = findViewById(R.id.suceesFullView)
    }

    fun selectPhotoFromCamera(view: View) {
        selectPicture(1)
    }

    fun submitForApproval(view: View) {
        hitRegestionApiCall(true);
    }

    fun hitRegestionApiCall(isVerified: Boolean){
        try {
            LocalModel.instance!!.showProgressDialog(mActivity, "")
            if (validation()) {
                try {
                    if (bitmapID==null){
                        bitmapID = BitmapFactory.decodeResource(applicationContext.getResources(),R.drawable.id_placeholder);
                    }
                } catch (e: Exception) {
                }


                var details = JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel, ""))
                details.put(StringUtils.IDPicture,Constants.encodeImage(bitmapID!!))
                var userType:String=""
                if (details.getString(StringUtils.userRole).equals( StringUtils.I_am_Buyer)){
                    userType = "1";
                }else{
                    userType = "2";
                }

                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                builder.addFormDataPart("name", details.getString(StringUtils.go_fullName))
                builder.addFormDataPart("phoneNo", details.getString(StringUtils.go_phoneNo))
                builder.addFormDataPart("profileVerified", isVerified.toString())
                builder.addFormDataPart("email", details.getString(StringUtils.go_emailID))
                builder.addFormDataPart("userType", userType)
                builder.addFormDataPart("fcmToken", "16500215025")
                builder.addFormDataPart("address_1", details.getString(StringUtils.go_address_1))
                builder.addFormDataPart("address_2", details.getString(StringUtils.go_address_2))
                builder.addFormDataPart("pincode", details.getString(StringUtils.go_Pincode))
                builder.addFormDataPart("city", details.getString(StringUtils.go_city))
                builder.addFormDataPart("state", details.getString(StringUtils.go_state))
                builder.addFormDataPart("latitude", details.getString(StringUtils.go_latitude))
                builder.addFormDataPart("longitude", details.getString(StringUtils.go_longitude))
                builder.addFormDataPart("Password", details.getString(StringUtils.go_passWord))
                val fileProfile = File(Constants.saveImagetoSDcard(convertBase64ToBitmap(details.getString(StringUtils.profilePicture))!!,this@IdentityVerificationActivity))
                builder.addFormDataPart("profilePicture", fileProfile.getName(), RequestBody.create(
                    MediaType.parse("multipart/form-data"), fileProfile))
                val IDPicture = File(Constants.saveImagetoSDcard(bitmapID!!,this@IdentityVerificationActivity))
                builder.addFormDataPart("IDPicture", fileProfile.getName(), RequestBody.create(
                    MediaType.parse("multipart/form-data"), IDPicture))

                LocalModel.instance!!.showProgressDialog(mActivity, "Loading..")
                val requestCall: Call<CommonModel> = App.instance!!.apiInterface!!.Registration(builder.build())
                requestCall.enqueue(object : Callback<CommonModel> {
                    override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
                        if (response.body() != null) {
                            LocalModel.instance!!.cancelProgressDialog()
                            if (response.body()?.success.toString().trim().equals("1")){
                                Constants.showDialog(mActivity,"Thank you "+JSONObject(App.instance!!.mPrefs!!.getString(StringUtils.signUpBundel,"")).getString(StringUtils.go_fullName) +"Your basic registration is done. Our team will verify your documents & will approve in 3 working days.",object :AlertTask{
                                    override fun doInPositiveClick() {
                                        suceesFullView!!.visibility=View.VISIBLE
                                        val handler = Handler()
                                        val runnable = Runnable {
                                            finish()
                                            val intent = Intent(mActivity, LoginActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            if (Build.VERSION.SDK_INT > 20) {
                                                val options = ActivityOptions.makeSceneTransitionAnimation(mActivity)
                                                startActivity(intent, options.toBundle())
                                            } else {
                                                startActivity(intent)
                                            }
                                        }
                                        handler.postDelayed(runnable, 3500)
                                    }
                                    override fun doInNegativeClick() {
                                    }
                                })

                            }else{
                                ToastUtils.shortToast("Registration Not Completed")
                            }

                        } else {
                            LocalModel.instance!!.cancelProgressDialog()
                        }
                    }
                    override fun onFailure(call: Call<CommonModel>, t: Throwable) {
                        LocalModel.instance!!.cancelProgressDialog()


                    }
                })
            }else{
                LocalModel.instance!!.cancelProgressDialog()
            }
        } catch (e: Exception) {
        }
    }


    private fun validation(): Boolean {
       /* if (bitmap == null) {
            ToastUtils.shortToast("Please Select A Profile Picture")
            Animation.editText_Sh(profileIV!!)
            return false
        } else*/ if (bitmapID == null) {
            ToastUtils.shortToast("Please Select A Aadhar Card")
            Animation.editText_Sh(attachedIDCart!!)
            return false
        } else if (enterFullName!!.text.isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Your FullName")
            Animation.editText_Sh(enterFullName!!)
            return false
        } else {
            return true
        }


    }

    private fun selectPicture(max: Int) {
        if (Permissons.isAllPermissionsGranted(mActivity)) {
            try {
                var intent = Intent(mActivity, ImagePickerActivity::class.java)
                intent.putExtra("Select_Max", max.toString())
                startActivityForResult(intent, AppConstants.PROFILE_PICTURE)

            } catch (e: Exception) {
                ToastUtils.shortToast("ImagePicker Error")
            }
        } else {
            Permissons.requestAllPermissions(mActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AppConstants.PROFILE_PICTURE -> {
                    var image_uris: ArrayList<Uri> = data!!.getParcelableArrayListExtra(
                        ImagePickerActivity.EXTRA_IMAGE_URIS
                    )
                    val bmOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(image_uris[0].path, bmOptions)
                    profileIV!!.setImageBitmap(bitmap)
                }
                AppConstants.ID_PICTURE -> {
                    var image_uris: ArrayList<Uri> = data!!.getParcelableArrayListExtra(
                        ImagePickerActivity.EXTRA_IMAGE_URIS
                    )
                    val bmOptions = BitmapFactory.Options()
                    bitmapID = BitmapFactory.decodeFile(image_uris[0].path, bmOptions)
                    attachedIDSUCCESS!!.visibility = View.VISIBLE
                    attachedIDCart!!.visibility = View.GONE
                    profileIV!!.setImageBitmap(bitmapID)
                }
            }

        }
    }

    fun selectIDImagePicker(view: View) {
        if (Permissons.isAllPermissionsGranted(mActivity)) {
            try {
                var intent = Intent(mActivity, ImagePickerActivity::class.java)
                intent.putExtra("Select_Max", "1")
                startActivityForResult(intent, AppConstants.ID_PICTURE)

            } catch (e: Exception) {
                ToastUtils.shortToast("ImagePicker Error")
            }
        } else {
            Permissons.requestAllPermissions(mActivity)
        }
    }

    private fun convertBase64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.decode(b64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

    fun skipForNow(view: View) {
        hitRegestionApiCall(false);
    }

    fun deleteIDProf(view: View) {

        attachedIDSUCCESS!!.visibility = View.GONE
        attachedIDCart!!.visibility = View.VISIBLE
        profileIV!!.setImageBitmap(null)
        bitmapID=null

    }
}