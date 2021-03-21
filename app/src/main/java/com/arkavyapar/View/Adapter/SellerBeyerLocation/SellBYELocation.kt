package com.arkavyapar.View.Adapter.SellerBeyerLocation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Model.MapUserFilter
import com.arkavyapar.R
import com.arkavyapar.Utils.StringUtils
import kotlinx.android.synthetic.main.child_seller_byer.view.*

class SellBYELocation (private val items: ArrayList<MapUserFilter>, val context: Context,val mCallback:MapFilterCallback) : RecyclerView.Adapter<SellBYELocation.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.child_seller_byer, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]

        if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Buyer)){
            holder.detailsInfoTV.text = i.distanceInKM+"km - "+i.list.size.toString()+" Sellers"
        }else if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Seller)){
            holder.detailsInfoTV.text = i.distanceInKM+"km - "+i.list.size.toString()+" Buyers"
        }

        holder.rootView.visibility = if (true) View.VISIBLE else View.GONE
        holder.rootView.setOnClickListener {
            mCallback.showUserToMap(i.list)
            i.isSelected=true
            for (i in 0 until items.size ){
                if(i!=position){
                    items.get(i).isSelected=false
                }
            }
            notifyDataSetChanged()
        }

        if (i.isSelected==true){
            holder.rootView.background=context.resources.getDrawable(R.drawable.bg_loc_select)
            holder.detailsInfoTV.setTextColor(context.resources.getColor(R.color.white))
        }else{
            holder.rootView.background=context.resources.getDrawable(R.drawable.bg_loc_unselect)
            holder.detailsInfoTV.setTextColor(context.resources.getColor(R.color.black))
        }


    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val rootView = view.rootView
        val detailsInfoTV = view.detailsInfoTV
        val parent = view
    }




}