package com.arkavyapar.View.Adapter.ProductAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.Constant.AppConstants
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.ProductListing
import com.arkavyapar.R
import com.arkavyapar.View.Interface.ProductListMannageCallback
import com.arkavyapar.View.UI.MyProductListing
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlinx.android.synthetic.main.child_product_listing.view.*
import java.util.*


class ProductListAdapter(
    private val items: ArrayList<ProductListing>,
    val context: Context,
    val productListMannageCallback: ProductListMannageCallback
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.child_product_listing,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]

        holder.catagoryDD!!.setOnClickListener({
            val menu = PopupMenu(context, holder.catagoryDD)
            for (s in MyProductListing.masterProductList) {
                menu.menu.add(s)
            }
            menu.setOnMenuItemClickListener  { item ->
                holder.catagoryDD!!.setText(item.title)
                true
            }
            menu.show()
        })



        holder.avalibleDateDD.isSelected = true
        holder.producctQYT.text = "Product " + (position + 1).toString()
        if (i.bitmap != null) {
            holder.selectProductIV.setImageBitmap(i.bitmap)
        }else{
            try {
                Glide.with(context)
                    .load(i.imageUrl)
                    .dontAnimate()
                    .centerCrop()
                    .error(R.drawable.ic_add)
                    .into(holder.selectProductIV)
            } catch (e: Exception) {
            }
        }

        holder.selectProductIV.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                productListMannageCallback.addPictures(position)
            }

        })
        holder.avalibleDateDD.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val builder = MaterialDatePicker.Builder.dateRangePicker()
                val now = Calendar.getInstance()
                builder.setSelection(
                    androidx.core.util.Pair(
                        now.timeInMillis,
                        now.timeInMillis + 172800000
                    )
                )
                val picker = builder.build()
                picker.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    picker.toString()
                )
                picker.addOnPositiveButtonClickListener(
                    MaterialPickerOnPositiveButtonClickListener<Pair<Long?, Long?>> { selection ->
                        val startDate: String =
                            Constants.convertMillisToDateString(
                                selection.first!!,
                                AppConstants.DT_FORMAT_10
                            )
                                .toString()
                        val endDate: String = Constants.convertMillisToDateString(
                            selection.second!!,
                            AppConstants.DT_FORMAT_10
                        )
                            .toString()
                        holder.avalibleDateDD.text = startDate + " - " + endDate
                    })
            }
        })
        holder.addMoreProduct.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
               // productListMannageCallback.addMoreItems()
            }

        })

        try {
            holder.avalibleDateDD.setText(items[position].avalibleDateDD)
            holder.catagoryDD.setText(items[position].category)
            holder.subcatagoryDD.setText(items[position].sub_category)
            holder.avalibleQytED.setText(items[position].avalibleQytED)
        } catch (e: Exception) {
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val producctQYT = view.producctQYT
        val selectProductIV = view.selectProductIV
        val catagoryDD = view.catagoryDD
        val addMoreProduct = view.addMoreProduct
        val subcatagoryDD = view.subcatagoryDD
        val avalibleQytED = view.avalibleQytED
        val avalibleDateDD = view.avalibleDateDD
        val parent = view
    }

}