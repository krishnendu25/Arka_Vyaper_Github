package com.arkavyapar.View.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.MarketPriceModel
import com.arkavyapar.Model.ReponseModel.AllSellerListApiModel
import com.arkavyapar.Model.ReponseModel.BannerApiModel
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.View.Adapter.HeaderBannerAdapter
import com.arkavyapar.View.Adapter.MarketPriceAdapter
import com.arkavyapar.View.Adapter.SellerListDashboard.AllSellerList
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class SellerDashBoard : Fragment() {
    var sellerProductList: RecyclerView? = null
    var headBanerList: RecyclerView? = null
    var viewAllRate: TextView? = null
    var marketPriceList: RecyclerView? = null
    var viewFilter: TextView? = null
    var viewFilterSeller: TextView? = null
    var buyerRequestList: RecyclerView? = null
    var buyerRequestView:LinearLayout? = null
    var sellerProductViewView:LinearLayout? = null
    var emptyView_buyerRequestList: TextView? = null
    var emptyView_sellerProductList: TextView? = null
    var myCurrentLocation:LatLng?= LatLng(22.0000,88.0000)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_seller_dash_board, container, false)
        iniView(view)
        hitGetAllBanner()

        hitGetAllMarketPrice()


        return view
    }

    private fun hitGetAllMarketPrice() {
        try {
            var marketPriceAdapter= MarketPriceAdapter(tempMarketPrice(),activity!!)
            marketPriceList!!.adapter = marketPriceAdapter
        } catch (e: Exception) {
        }
    }

    private fun hitGetAllBanner() {
        try {
            val requestCall: Call<BannerApiModel> = App.instance!!.apiInterface!!.getBannerHeader()
            requestCall.enqueue(object : Callback<BannerApiModel> {
                override fun onResponse(
                    call: Call<BannerApiModel>,
                    response: Response<BannerApiModel>
                ) {
                    if (response.body() != null) {
                        LocalModel.instance!!.cancelProgressDialog()
                        if (response.body()?.success.toString().trim().equals("1")) {
                            try {
                                var headerViewListAdapter = HeaderBannerAdapter(
                                    response.body()?.BannerList!!,
                                    activity!!
                                )
                                headBanerList!!.adapter = headerViewListAdapter
                            } catch (e: Exception) {
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BannerApiModel>, t: Throwable) {}
            })
        } catch (e: Exception) {
        }
    }

    private fun iniView(view: View) {
        headBanerList = view.findViewById(R.id.headBanerList)
        sellerProductList = view.findViewById(R.id.sellerProductList)
        viewAllRate = view.findViewById(R.id.viewAllRate)
        marketPriceList = view.findViewById(R.id.marketPriceList)
        viewFilter = view.findViewById(R.id.viewFilter)
        emptyView_buyerRequestList = view.findViewById(R.id.emptyView_buyerRequestList)
        emptyView_sellerProductList = view.findViewById(R.id.emptyView_sellerProductList)
        viewFilterSeller = view.findViewById(R.id.viewFilterSeller)
        buyerRequestList = view.findViewById(R.id.buyerRequestList)
        Constants.setLayoutManager(headBanerList!!, true, false)
        Constants.setLayoutManager(marketPriceList!!, true, false)
        Constants.setLayoutManager(buyerRequestList!!, false, true)
        Constants.setLayoutManager(sellerProductList!!, false, true)
        marketPriceList!!.getLayoutManager()!!.scrollToPosition(Integer.MAX_VALUE / 2);
        buyerRequestView  = view.findViewById(R.id.buyerRequestView)
        sellerProductViewView  = view.findViewById(R.id.sellerProductViewView)
        if (App.instance!!.mPrefs!!.getString(StringUtils.userRole,"").equals(StringUtils.I_am_Buyer)){
            sellerProductViewView!!.visibility=View.VISIBLE
            buyerRequestView!!.visibility=View.GONE
        }else if (App.instance!!.mPrefs!!.getString(StringUtils.userRole,"").equals(StringUtils.I_am_Seller)){
            sellerProductViewView!!.visibility=View.GONE
            buyerRequestView!!.visibility=View.VISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(myCurrentLocation_: LatLng) =
            SellerDashBoard().apply {
            this.myCurrentLocation=myCurrentLocation_
            }
    }


    fun tempMarketPrice(): ArrayList<MarketPriceModel> {
        var list = ArrayList<MarketPriceModel>()

        val price: List<String> = Arrays.asList(
            "160.00",
            "180.00",
            "130.00",
            "240.00",
            "29.00",
            "25.00",
            "46.00",
            "58.00",
            "42.00",
            "64.00",
            "52.00",
            "550.00"
        )
        val name: List<String> = Arrays.asList(
            "APPLE",
            "APPLE SIMLA",
            "APPLE MISRI",
            "APPLE WASHINGTON",
            "TAIWAN GELLY",
            "BANANA PACHABALE",
            "BANANA YELLAKKI",
            "BANAN CHANDRA",
            "BANAN NENDEA",
            "CHAKKOTHA FRUIT",
            "BANANA RASABLE",
            "DRY APRICOT"
        )

        for (i in 0 until price.size ){

           var model = MarketPriceModel()
            model.Item_Code=i.toString()
            model.Item_Name=name[i]
            model.Unit_Price=price[i]
            list.add(model)

        }
        return list
    }
    private fun HitAllSellerList() {
        var requestCall: Call<AllSellerListApiModel>?=null
        if (App.instance!!.mPrefs!!.getString(StringUtils.userRole,"").equals(StringUtils.I_am_Buyer)){
             requestCall = App.instance!!.apiInterface!!.getAllSellerListApi(
                myCurrentLocation!!.latitude.toString(), myCurrentLocation!!.longitude.toString(),  App.instance!!.mPrefs!!.getString(
                    StringUtils.userID,
                    ""
                ).toString()
            )
            sellerProductList!!.visibility=View.VISIBLE
            buyerRequestList!!.visibility=View.GONE
        }else{
             requestCall = App.instance!!.apiInterface!!.getAllByerListApi(
                myCurrentLocation!!.latitude.toString(), myCurrentLocation!!.longitude.toString(),  App.instance!!.mPrefs!!.getString(
                    StringUtils.userID,
                    ""
                ).toString()
            )
            sellerProductList!!.visibility=View.GONE
            buyerRequestList!!.visibility=View.VISIBLE
        }

        requestCall.enqueue(object : Callback<AllSellerListApiModel> {
            override fun onResponse(call: Call<AllSellerListApiModel>, response: Response<AllSellerListApiModel>) {
                if (response.body() != null) {
                    if (response.body()?.success.toString().trim().equals("1")) {
                        try {
                            var allSellerList =   AllSellerList(response.body()?.Seller_List!!,activity!!)
                            sellerProductList!!.adapter=allSellerList
                            buyerRequestList!!.adapter=allSellerList

                            if (response.body()?.Seller_List!!.size>0){
                                emptyView_buyerRequestList!!.visibility=View.GONE
                                emptyView_sellerProductList!!.visibility=View.GONE
                            }


                        } catch (e: Exception) {
                        }
                    }
                }
            }
            override fun onFailure(call: Call<AllSellerListApiModel>, t: Throwable) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        HitAllSellerList()
    }
}