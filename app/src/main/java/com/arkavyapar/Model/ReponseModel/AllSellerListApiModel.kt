package com.arkavyapar.Model.ReponseModel
data class AllSellerListApiModel(
    val Seller_List: List<Seller>,
    val msg: String,
    val success: Int
)

data class Seller(
    val Address1: String,
    val Address2: String,
    val City: String,
    val Email: String,
    val Idpic: String,
    val Isverified: String,
    val Latitude: String,
    val Longitude: String,
    val PhoneNo: String,
    val Product_list: List<Product>,
    val Productnamelist: String,
    val ProfilePic: String,
    val State: String,
    val UserId: String,
    val UserName: String,
    val Usertype: String
)

data class Product(
    val Qty: String,
    val availabledate: String,
    val pid: String,
    val product_Details: String,
    val productname: String,
    val productpicture: String,
    val user_id: String
)