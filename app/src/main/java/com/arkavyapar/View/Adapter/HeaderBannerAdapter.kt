package com.arkavyapar.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.child_banner_header.view.*

class HeaderBannerAdapter(private val items: List<String>, val context: Context) : RecyclerView.Adapter<HeaderBannerAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.child_banner_header,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        Glide.with(context)
            .load(items[position]) //   .override(selected_bottom_size, selected_bottom_size)
            .dontAnimate()
            .centerCrop()
            .error(R.drawable.ic_splash)
            .into(holder.bannerPictureTV)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bannerPictureTV = view.bannerPictureTV
        val parent = view
    }
}