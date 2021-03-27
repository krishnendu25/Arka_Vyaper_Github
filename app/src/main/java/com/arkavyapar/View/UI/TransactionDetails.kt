package com.arkavyapar.View.UI

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Model.RequestListModel
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.View.Adapter.RequestListAdapter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionDetails : AppCompatActivity() {
    var transaction_history: RecyclerView? = null
    var mActivity: Activity?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)
        iniview()
        hitGetMyRequest()
    }
    private fun iniview() {
        mActivity =   this@TransactionDetails
        transaction_history = findViewById(R.id.transaction_history);
        var  linearLayoutManager = LinearLayoutManager(App.instance, LinearLayoutManager.VERTICAL, false)
        transaction_history!!.layoutManager = linearLayoutManager
    }



    private fun hitGetMyRequest() {
        val requestCall: Call<ResponseBody> = App.instance!!.apiInterface!!.RequestAllProduct(
            App.instance!!.mPrefs!!.getString(
                StringUtils.userID,
                ""
            ).toString()
        )
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.body() != null) {

                    if (response.isSuccessful){
                        LocalModel.instance!!.cancelProgressDialog()
                        var jsonObject = JSONObject(response.body().string());
                        if (jsonObject.getString("success").toString().trim().equals("1")){
                            var userModel = jsonObject.getJSONArray("USERDETAILS")
                            var productList = ArrayList<RequestListModel>()
                            for (i in 0 until userModel.length()) {
                                var index = userModel.getJSONObject(i)
                                var productmodel = RequestListModel()
                                productmodel.ADDRESS1 = index.getString("ADDRESS1")
                                productmodel.ADDRESS2 = index.getString("ADDRESS2")
                                productmodel.CITY = index.getString("CITY")
                                productmodel.EMAIL = index.getString("EMAIL")
                                productmodel.IDPICPATH = index.getString("IDPICPATH")
                                productmodel.MESSAGE = index.getString("MESSAGE")
                                productmodel.PHONENO = index.getString("PHONENO")
                                productmodel.PINCODE = index.getString("PINCODE")
                                productmodel.PROFILEPICPATH = index.getString("PROFILEPICPATH")
                                productmodel.PROFILEVERIFIED = index.getString("PROFILEVERIFIED")
                                productmodel.STATE = index.getString("STATE")
                                productmodel.TRANSACTIONFLAG = index.getString("TRANSACTIONFLAG")
                                productmodel.USERLANG = index.getString("USERLANG")
                                productmodel.USERLAT = index.getString("USERLAT")
                                productmodel.USERNAME = index.getString("USERNAME")
                                productmodel.DATE_TIME = index.getString("DATE_TIME")
                                if (index.getString("TRANSACTIONFLAG").equals("true")){
                                    productList!!.add(productmodel)
                                }
                            }
                            var adapter = RequestListAdapter(productList, mActivity!!,true)
                            transaction_history!!.adapter=adapter;
                        } else {
                            LocalModel.instance!!.cancelProgressDialog()
                        }
                    }
                } else {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })


    }





}