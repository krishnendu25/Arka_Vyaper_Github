package com.arkavyapar.Model.ReponseModel

data class ByerLocation(
    val Location_List: List<BuyerLocation>,
    val msg: String,
    val success: Int
)

data class BuyerLocation(
    val Latitude: String,
    val Longitude: String,
    val UserId: String,
    val UserName: String
)