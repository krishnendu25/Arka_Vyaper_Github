package com.arkavyapar.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.Model.MarketPriceModel
import com.arkavyapar.R
import kotlinx.android.synthetic.main.child_product_today.view.*

class MarketPriceAdapter (private val items: ArrayList<MarketPriceModel>, val context: Context) : RecyclerView.Adapter<MarketPriceAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.child_product_today, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.nameProductTV.isSelected=true
        holder.subNameProductTV.isSelected=true
        holder.nameProductTV.text = i.Item_Name
        holder.subNameProductTV.text = i.Item_Name
        holder.currentProductTV.text = "₹"+i.Unit_Price
        holder.parent.setOnClickListener {

        }
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val nameProductTV = view.NameProductTV
        val subNameProductTV = view.SubNameProductTV
        val currentProductTV = view.currentProductTV
        val parent = view
    }
}