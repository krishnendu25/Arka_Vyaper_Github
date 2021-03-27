package com.arkavyapar.Model

import android.os.Parcel
import android.os.Parcelable

class ProductModel() :Parcelable {
 
        var PID: String?=null
        var Productpic: String?=null
        var availabledate: String?=null
        var id: String?=null
        var product_Details: String?=null
        var productname: String?=null
        var qty: String?=null

    constructor(parcel: Parcel) : this() {
        PID = parcel.readString()
        Productpic = parcel.readString()
        availabledate = parcel.readString()
        id = parcel.readString()
        product_Details = parcel.readString()
        productname = parcel.readString()
        qty = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
       dest!!.writeString(PID)
        dest!!.writeString(Productpic)
        dest!!.writeString(availabledate)
        dest!!.writeString(id)
        dest!!.writeString(product_Details)
        dest!!.writeString(productname)
        dest!!.writeString(qty)
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }

}