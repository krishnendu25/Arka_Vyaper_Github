package com.arkavyapar.View.UI

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Constant.AppConstants
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.ProductListing
import com.arkavyapar.Model.ReponseModel.CommonModel
import com.arkavyapar.Model.ReponseModel.MyProductListApi
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.Permissons
import com.arkavyapar.Utils.PhotoPicker.ImagePickerActivity
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.View.Adapter.ProductAdapter.ProductListAdapter
import com.arkavyapar.View.Interface.ProductListMannageCallback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class MyProductListing : AppCompatActivity() {
    var myProductList: RecyclerView? = null
    var productAdapter: ProductListAdapter? = null
    var productArrayList: ArrayList<ProductListing>? = ArrayList<ProductListing>()
    var selectedPosition: Int = -1

    companion object {
        var masterProductList: ArrayList<String> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product_listing)
        iniView()
        HitGetmyproduct()
        hitGetAllProduct()
    }

    private fun iniView() {
        myProductList = findViewById(R.id.myProductList)
        Constants.setLayoutManager(myProductList!!, false, true)
        productAdapter = ProductListAdapter(
            productArrayList!!,
            this@MyProductListing,
            object : ProductListMannageCallback {
                override fun addMoreItems() {

                }


                override fun addPictures(position: Int) {
                    selectedPosition = position
                    selectPicture(position)
                }

                override fun deleteThisItems(position: Int) {

                }

            })
        myProductList!!.adapter = productAdapter
    }

    private fun selectPicture(position: Int) {

        if (Permissons.isAllPermissionsGranted(this@MyProductListing)) {
            try {
                var intent = Intent(this@MyProductListing, ImagePickerActivity::class.java)
                intent.putExtra("Select_Max", "1")
                startActivityForResult(intent, AppConstants.PROFILE_PICTURE)

            } catch (e: Exception) {
                ToastUtils.shortToast("ImagePicker Error")
            }
        } else {
            Permissons.requestAllPermissions(this@MyProductListing)
        }
    }


    fun setAddProducts(view: View) {
        var model = ProductListing()
        productArrayList!!.add(model)
        if (productAdapter != null)
            productAdapter!!.notifyDataSetChanged()
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
                    val bitmap = BitmapFactory.decodeFile(image_uris[0].path, bmOptions)
                    /*   var productModel = ProductListing()
                    productModel.imageBase64 = Constants.encodeImage(bitmap)
                    productModel.bitmap = bitmap
                    productArrayList!!.add(selectedPosition,productModel)*/
                    productArrayList!!.get(selectedPosition).bitmap = bitmap
                    productArrayList!!.get(selectedPosition).imageBase64 = Constants.encodeImage(
                        bitmap
                    )

                    if (productAdapter != null)
                        productAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    fun saveProduct(view: View): Boolean {
        var productListing = ArrayList<ProductListing>()
        for (i in 0 until productArrayList!!.size) {
            val v: View = myProductList!!.getChildAt(i)
            val vv = (v as RelativeLayout).getChildAt(0)
            val catagoryDD: TextView = vv.findViewById(R.id.catagoryDD)
            val subcatagoryDD: EditText = vv.findViewById(R.id.subcatagoryDD)
            val avalibleQytED: EditText = vv.findViewById(R.id.avalibleQytED)
            val avalibleDateDD: TextView = vv.findViewById(R.id.avalibleDateDD)
            val selectProductIV: ImageView = vv.findViewById(R.id.selectProductIV)
            val bitmapPicture = try {
                selectProductIV.drawable.toBitmap()
            } catch (e: Exception) {
                ToastUtils.shortToast("Please Select Product Image")
                return false
            }

            if (avalibleDateDD.text.isNullOrBlank()) {
                ToastUtils.shortToast("Please Select Date Range")
                return false
            }
            if (catagoryDD.text.trim().isNullOrBlank()) {
                ToastUtils.shortToast("Please Enter catagory")
                return false
            }
            if (subcatagoryDD.text.trim().isNullOrBlank()) {
                ToastUtils.shortToast("Please Enter sub catagory")
                return false
            }
            if (avalibleQytED.text.trim().isNullOrBlank()) {
                ToastUtils.shortToast("Please Enter Quantity")
                return false
            }

            if (bitmapPicture == null) {
                ToastUtils.shortToast("Please Select Product Image")
                return false
            }
            val productList = ProductListing()
            try {
                productList.avalibleDateDD = avalibleDateDD.text.trim().toString()
                productList.sub_category = subcatagoryDD.text.trim().toString()
                productList.category = catagoryDD.text.trim().toString()
                productList.avalibleQytED = avalibleQytED.text.trim().toString()
                productList.bitmap = bitmapPicture
                productList.imageBase64 = Constants.encodeImage(bitmapPicture)
            } catch (e: Exception) {
            }
            productListing.add(productList)

        }

        if (productListing.size > 0) {
            hitAddMyProduct(productListing)

        }


        return true
    }

    private fun hitAddMyProduct(productListing: ArrayList<ProductListing>) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        for (i in 0 until productListing.size) {
            builder.addFormDataPart("ProductName[]", productListing.get(i).category)
            builder.addFormDataPart("ProductDetails[]", productListing.get(i).sub_category)
            builder.addFormDataPart("ProductQyt[]", productListing.get(i).avalibleQytED)
            builder.addFormDataPart("avalibleDateRange[]", productListing.get(i).avalibleDateDD)
            builder.addFormDataPart(
                "userID[]",
                App.instance!!.mPrefs!!.getString(StringUtils.userID, "").toString()
            )

            val fileProfile = File(
                Constants.saveImagetoSDcard(
                    productListing.get(i).bitmap!!,
                    this@MyProductListing
                )
            )
            builder.addFormDataPart(
                "productPicture[]", fileProfile.name, RequestBody.create(
                    MediaType.parse("multipart/form-data"), fileProfile
                )
            )
        }
        LocalModel.instance!!.showProgressDialog(this@MyProductListing, "Loading..")
        val requestCall: Call<CommonModel> =
            App.instance!!.apiInterface!!.productAddToMyProfile(builder.build())
        requestCall.enqueue(object : Callback<CommonModel> {
            override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")) {
                        ToastUtils.shortToast("Product added successfully")
                        HitGetmyproduct()
                    } else {
                        ToastUtils.shortToast("Product Not added. Try Again")
                    }

                } else {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<CommonModel>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })

    }

    private fun hitGetAllProduct() {
        try {
            val requestCall: Call<ResponseBody> =
                App.instance!!.apiInterface!!.getproductDropDownModel()
            requestCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.body() != null) {
                        var jsonObject = JSONObject(response.body().string())
                        var product_List = jsonObject.getJSONArray("Product ist")
                        masterProductList.clear()
                        for (i in 0 until product_List.length()) {
                            var jsonObject = product_List.get(i).toString()
                            masterProductList.add(jsonObject)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
        } catch (e: Exception) {
        }
    }


    private fun HitGetmyproduct() {
        val requestCall: Call<MyProductListApi> =
            App.instance!!.apiInterface!!.productfetchToMyProfile(
                App.instance!!.mPrefs!!.getString(
                    StringUtils.userID,
                    ""
                ).toString()
            )
        requestCall.enqueue(object : Callback<MyProductListApi> {
            override fun onResponse(
                call: Call<MyProductListApi>,
                response: Response<MyProductListApi>
            ) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")) {
                        productArrayList!!.clear()
                        for (i in 0 until response.body()?.productdetails!!.PID.size) {
                            var model = ProductListing()
                            try {
                                model.avalibleDateDD =
                                    response.body()?.productdetails!!.AVAILABLEDATE[i]
                                model.avalibleQytED = response.body()?.productdetails!!.QTY[i]
                                model.sub_category =
                                    response.body()?.productdetails!!.PRODUCTDETAILS[i]
                                model.category = response.body()?.productdetails!!.PRODUCTNAME[i]
                                model.imageUrl = response.body()?.productdetails!!.PRODUCTPICTURE[i]
                                model.pid = response.body()?.productdetails!!.PID[i]
                            } catch (e: Exception) {
                            }
                            productArrayList!!.add(model)
                        }
                        if (productAdapter != null)
                            productAdapter!!.notifyDataSetChanged()

                    } else {
                        LocalModel.instance!!.cancelProgressDialog()
                    }
                } else {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<MyProductListApi>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })


    }


}