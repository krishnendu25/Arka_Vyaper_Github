package com.arkavyapar.View.Adapter.SellerListDashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.ProductModel
import com.arkavyapar.Model.ReponseModel.Seller
import com.arkavyapar.R
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.Utils
import com.arkavyapar.View.UI.RequestToUserActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.child_buyer_list.view.*

class AllSellerList (private val items: List<Seller>, val context: Context) : RecyclerView.Adapter<AllSellerList.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.child_buyer_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sellerModel = items[position]
        Glide.with(context).load(sellerModel.ProfilePic).into(holder.profilePictureIV)
        holder.buyerTV.text = Utils.toTitleCase(sellerModel.UserName)
        holder.addressTV.text =Utils.toTitleCase( sellerModel.Address1)
        holder.productTV.text = Utils.toTitleCase(sellerModel.Productnamelist)
        if (!sellerModel.Isverified.equals("")){
            if (sellerModel.Isverified.equals("true")){
                holder.tagView.visibility=View.VISIBLE
            }else{
                holder.tagView.visibility=View.GONE
            }
        }else{
            holder.tagView.visibility=View.GONE
        }

        if (sellerModel.Usertype.equals("1")){
         holder.tagView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_blue_tag))
        }else if (sellerModel.Usertype.equals("2")){
            holder.tagView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_yellow_tag))
        }


        holder.parent.setOnClickListener {
          var productList = ArrayList<ProductModel>();
            for (i in 0 until sellerModel.Product_list.size){
                var model =ProductModel()
                model.PID=sellerModel.Product_list.get(i).pid
                model.Productpic=sellerModel.Product_list.get(i).productpicture
                model.availabledate=sellerModel.Product_list.get(i).availabledate
                model.id=sellerModel.Product_list.get(i).user_id
                model.product_Details=sellerModel.Product_list.get(i).product_Details
                model.productname=sellerModel.Product_list.get(i).productname
                model.qty=sellerModel.Product_list.get(i).Qty
                productList!!.add(model)
            }
            
            
            
        var intent = Intent(context, RequestToUserActivity::class.java)
            intent.putExtra(StringUtils.userID,sellerModel.UserId)
            intent.putExtra(StringUtils.productList,sellerModel.Productnamelist)
            intent.putExtra(StringUtils.productList_array,productList)
        context.startActivity(intent)
        }
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val profilePictureIV = view.profilePictureIV
        val buyerTV = view.buyerTV
        val addressTV = view.addressTV
        val tagView = view.tagView
        val productTV = view.productTV
        val parent = view
    }
}