package com.arkavyapar.View.UI

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.ProductModel
import com.arkavyapar.Model.ReponseModel.CommonModel
import com.arkavyapar.Model.ReponseModel.LoginModel
import com.arkavyapar.Model.ReponseModel.UserDetails
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import com.arkavyapar.View.Adapter.ProductShowCase
import com.arkavyapar.View.Interface.AlertTask
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RequestToUserActivity : AppCompatActivity() {
    var productList = ArrayList<ProductModel>();
    var profileIV: CircleImageView? = null
    var seller_Name_TV: TextView? = null
    var seller_Location_TV: TextView? = null
    var seller_product_list_TV: TextView? = null
    var seller_product_list: RecyclerView? = null
    var myPhoneNo: EditText? = null
    var needDD: TextView? = null
    var mActivity:Activity?=null
    var requirement_ED: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_to_user)
        iniView()
        hitgetUser(true,intent.getStringExtra(StringUtils.userID));
    }

    private fun iniView() {
        productList = intent.getParcelableArrayListExtra(StringUtils.productList_array)
        mActivity = this@RequestToUserActivity
        profileIV =  findViewById(R.id.profileIV);
        seller_Name_TV =  findViewById(R.id.seller_Name_TV);
        seller_Location_TV =  findViewById(R.id.seller_Location_TV);
        seller_product_list_TV =  findViewById(R.id.seller_product_list_TV);
        seller_product_list =  findViewById(R.id.seller_product_list);
        Constants.setLayoutManager(seller_product_list!!, true, false)
        myPhoneNo =  findViewById(R.id.myPhoneNo);
        needDD =  findViewById(R.id.needDD);
        requirement_ED =  findViewById(R.id.requirement_ED);
        var productShowCase =  ProductShowCase(productList,mActivity!!);
        seller_product_list!!.adapter = productShowCase!!
    }
    private fun hitgetUser(isLoderShow: Boolean, userid: String){
        if (isLoderShow){
            LocalModel.instance!!.showProgressDialog(mActivity, "Getting location....")
        }

        val requestCall: Call<UserDetails> = App.instance!!.apiInterface!!.userdetails(userid)
        requestCall.enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")) {
                        seller_Name_TV!!.setText(response.body()?.userdetails!!.USERNAME)
                        seller_Location_TV!!.setText(response.body()?.userdetails!!.STATE)
                        seller_product_list_TV!!.setText(intent.getStringExtra(StringUtils.productList))
                        Utils.setImageFromUrl(profileIV!!, response.body()?.userdetails!!.PROFILEPIC!!, mActivity!!)
                    }
                }
            }

            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })
    }
    fun submitTheRequest(view: View) {
        if(validation()) {
            Constants.showAlertDialog(this@RequestToUserActivity,"are you have ever transaction with this user?","Make Sure", object :
                AlertTask {
                override fun doInPositiveClick() {
                    submitTheRequest(true)
                }

                override fun doInNegativeClick() {
                    submitTheRequest(false)
                }
            })
        }

    }

    fun submitTheRequest(isPurces:Boolean) {

            var json:JSONObject = JSONObject();
            json.put("PHONE NO",myPhoneNo!!.text.toString())
            json.put("REQ",requirement_ED!!.text.toString())
            json.put("NEED",needDD!!.text.toString())
            LocalModel.instance!!.showProgressDialog(this@RequestToUserActivity, "Loading..")
            val requestCall: Call<CommonModel> = App.instance!!.apiInterface!!.RequestToUserActivity(
                App.instance!!.mPrefs!!.getString(
                    StringUtils.userID,""),"0",intent.getStringExtra(StringUtils.userID),isPurces.toString(),json.toString()
            )
            requestCall.enqueue(object : Callback<CommonModel> {
                override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
                    if (response.body() != null) {
                        LocalModel.instance!!.cancelProgressDialog()
                        if (response.body()?.success.toString().trim().equals("1")){
                            ToastUtils.shortToast("Request Successful")
                            finish()

                        } else {
                            ToastUtils.shortToast("Request Unsuccessful")
                            LocalModel.instance!!.cancelProgressDialog()
                        }
                    } else {
                        ToastUtils.shortToast("Request Unsuccessful")
                        LocalModel.instance!!.cancelProgressDialog()
                    }
                }
                override fun onFailure(call: Call<CommonModel>, t: Throwable) {
                    LocalModel.instance!!.cancelProgressDialog()
                    ToastUtils.shortToast("Request Unsuccessful")
                }
            })



    }


    fun openNeedMenu(view: View) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.requirement, menu)
            setOnMenuItemClickListener { item ->
                needDD!!.text=item.title

                true
            }
        }.show()
    }

    private fun validation(): Boolean {
        if (requirement_ED!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Address 1")
            Animation.editText_Sh(requirement_ED!!)
            return false
        }else if (needDD!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Select Why do you need this?")
            Animation.editText_Sh(needDD!!)
            return false
        }else if (myPhoneNo!!.text.isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter any Specific Requirement")
            Animation.editText_Sh(myPhoneNo!!)
            return false
        }
        else {
            return true
        }
    }


    fun showNotification(title:String,msg:String){





    }



}