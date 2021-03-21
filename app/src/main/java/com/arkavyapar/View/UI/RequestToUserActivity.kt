package com.arkavyapar.View.UI

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.Constant.Constants
import com.arkavyapar.R
import com.arkavyapar.Utils.Utils
import de.hdodenhof.circleimageview.CircleImageView


class RequestToUserActivity : AppCompatActivity() {
    var profileIV: CircleImageView? = null
    var seller_Name_TV: TextView? = null
    var seller_Location_TV: TextView? = null
    var seller_product_list: RecyclerView? = null
    var myPhoneNo: EditText? = null
    var needDD: TextView? = null
    var requirement_ED: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_to_user)
        iniView()
    }

    private fun iniView() {
        profileIV =  findViewById(R.id.profileIV);
        seller_Name_TV =  findViewById(R.id.seller_Name_TV);
        seller_Location_TV =  findViewById(R.id.seller_Location_TV);
        seller_product_list =  findViewById(R.id.seller_product_list);
        Constants.setLayoutManager(seller_product_list!!, true, false)
        myPhoneNo =  findViewById(R.id.myPhoneNo);
        needDD =  findViewById(R.id.needDD);
        requirement_ED =  findViewById(R.id.requirement_ED);
    }

    fun submitTheRequest(view: View) {}
}