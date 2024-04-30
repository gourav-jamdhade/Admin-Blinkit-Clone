package com.example.adminblinkitclone.models

import com.example.userblinkitclone.roomdb.CartProducts


data class Orders(

    val orderId:String? =null,
    val orderList:List<CartProducts>? =null,
    val userAddress:String? =null,
    val orderStatus:Int? =null,
    val orderDate:String? =null,
    val orderUserId:String? =null,
)
