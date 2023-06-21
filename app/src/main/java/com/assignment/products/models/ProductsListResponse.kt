package com.assignment.products.models

import com.google.gson.annotations.SerializedName

/*data class ProductsListResponse(

    @field:SerializedName("ProductsListResponse")
    val productsListResponse: List<ProductsListData?>? = null
)*/

data class ProductsListData(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("product_type")
    val productType: String? = null,

    @field:SerializedName("price")
    val price: Double? = null,

    @field:SerializedName("tax")
    val tax: Double? = null,

    @field:SerializedName("product_name")
    val productName: String? = null
)
