package com.arkavyapar.View.Adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.Model.RequestListModel
import com.arkavyapar.R
import com.arkavyapar.Utils.Utils
import kotlinx.android.synthetic.main.child_request_list.view.*
import org.json.JSONObject


class RequestListAdapter(
    private val items: ArrayList<RequestListModel>,
    val context: Activity,
    val transition_: Boolean
) : RecyclerView.Adapter<RequestListAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.child_request_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        if (transition_){
            holder.transactionTV.text="Transaction Done"
        }else{
            holder.transactionTV.visibility=View.GONE
        }
        holder.userName.text = "Name: "+i.USERNAME
        holder.requestDate.text = "Request Date: "+i.DATE_TIME
        holder.phoneNoTV.text = "Phone No: "+i.PHONENO
        try {
            holder.needTv.text = "Need: "+ JSONObject(i.MESSAGE).getString("NEED")
            holder.requirementTV.text = "Requirement: "+ JSONObject(i.MESSAGE).getString("REQ")
        } catch (e: Exception) {
        }
        holder.profilePicture.visibility = if (true) View.VISIBLE else View.GONE
        holder.parent.setOnClickListener {

        }
        Utils.setImageFromUrl(
            holder.profilePicture!!,
            i.PROFILEPICPATH.toString(), context
        )

        holder.call_now.setOnClickListener({
            try {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + i.PHONENO)
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE), 1254)
                } else {
                    context.startActivity(callIntent)
                }
            } catch (e: Exception) {
            }

        })


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePicture = view.profilePicture
        val userName = view.userName
        val needTv = view.needTv
        val phoneNoTV = view.phoneNoTV
        val requestDate = view.requestDate
        val call_now = view.call_now
        val requirementTV = view.requirementTV
        val transactionTV = view.transactionTV
        val parent = view
    }
}