package com.arkavyapar.View.Adapter.SellerListDashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Constant.Constants
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
        val i = items[position]
        Glide.with(context).load(i.ProfilePic).into(holder.profilePictureIV)
        holder.buyerTV.text = Utils.toTitleCase(i.UserName)
        holder.addressTV.text =Utils.toTitleCase( i.Address1)
        holder.productTV.text = Utils.toTitleCase(i.Productnamelist)
        if (!i.Isverified.equals("")){
            if (i.Isverified.equals("true")){
                holder.tagView.visibility=View.VISIBLE
            }else{
                holder.tagView.visibility=View.GONE
            }
        }else{
            holder.tagView.visibility=View.GONE
        }

        if (i.Usertype.equals("1")){
         holder.tagView.setBackgroundResource(R.drawable.ic_blue_tag)
        }else{
            holder.tagView.setBackgroundResource(R.drawable.ic_yellow_tag)
        }


        holder.parent.setOnClickListener {
        var intent = Intent(context, RequestToUserActivity::class.java)
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