package com.example.userblinkitclone.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey



data class CartProducts(

    var productId: String = "random",

    var productTitle: String? = null,
    var productQuantity: String? = null,
    var productPrice: String? = null,
    var productStock: Int? = null,
    var productCategory: String? = null,
    var productCount: Int? = null,
    var adminUid: String? = null,
    var productImage: String? = null,
    var productType: String? = null,
)
