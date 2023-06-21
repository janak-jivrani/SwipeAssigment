package com.assignment.products.models

data class AddProductRequest(
    val product_type: String,
    val product_name: String,
    val price: String,
    val tax: String,
    val files: String
)
