package com.arkavyapar.Model.ReponseModel

data class MyProductListApi(
    val msg: String,
    val productdetails: Productdetails,
    val success: Int
)

data class Productdetails(
    val AVAILABLEDATE: List<String>,
    val PID: List<String>,
    val PRODUCTDETAILS: List<String>,
    val PRODUCTNAME: List<String>,
    val PRODUCTPICTURE: List<String>,
    val QTY: List<String>,
    val USERID: List<String>
)