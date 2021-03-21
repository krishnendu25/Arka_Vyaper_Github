package com.arkavyapar.Model.ReponseModel

data class UserDetails(
    val msg: String,
    val success: Int,
    val userdetails: UserdetailsX
)

data class UserdetailsX(
    val ADDRESS1: String,
    val ADDRESS2: String,
    val CITY: String,
    val CURENTLANG: String,
    val CURENTLAT: String,
    val EMAIL: String,
    val IDPIC: String,
    val PHONENO: String,
    val PINCODE: String,
    val PROFILEPIC: String,
    val Profileverified: String,
    val STATE: String,
    val UID: String,
    val USERNAME: String,
    val USERPERMANENTLANG: String,
    val USERPERMANENTLAT: String,
    val USERTYPE: String
)