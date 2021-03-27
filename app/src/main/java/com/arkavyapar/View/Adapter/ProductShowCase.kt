package com.arkavyapar.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.Model.ProductModel
import com.arkavyapar.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.child_product_show.view.*

class ProductShowCase (private val items: ArrayList<ProductModel>, val context: Context) : RecyclerView.Adapter<ProductShowCase.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.child_product_show, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        Glide.with(context).load(i.Productpic).placeholder(R.drawable.ic_place).into(holder.productImage)
        holder.productImage.visibility = if (true) View.VISIBLE else View.GONE
        holder.productName.isSelected=true
        holder.productName.text = i.productname
        holder.productQynt.isSelected=true
        holder.productQynt.text = i.qty+" Quantity"
        holder.parent.setOnClickListener {

        }
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val productImage = view.productImage
        val productName = view.productName
        val productQynt = view.ProductQynt
        val parent = view
    }
}