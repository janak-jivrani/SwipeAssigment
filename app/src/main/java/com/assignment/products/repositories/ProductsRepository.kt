package com.assignment.products.repositories

import com.assignment.products.api.RetrofitService
import com.assignment.products.models.AddProductRequest
import com.assignment.products.models.AddProductResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class ProductsRepository constructor(private val retrofitService: RetrofitService)  {

    suspend fun getProducts() = retrofitService.getProducts()

    suspend fun addProduct(addProductReq: AddProductRequest):Response<AddProductResponse> {
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap["product_type"] =
            addProductReq.product_type.toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap["product_name"] =
            addProductReq.product_name.toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap["price"] =
            addProductReq.price.toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap["tax"] =
            addProductReq.tax.toRequestBody("text/plain".toMediaTypeOrNull())

        val file = File(addProductReq.files)
        val body: MultipartBody.Part = if (file.exists()) {
            MultipartBody.Part.createFormData("files[0]", file.name, file.asRequestBody())
        } else {
            MultipartBody.Part.createFormData(
                "files", "", "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }
        return retrofitService.addProduct(hashMap, body)
    }

}