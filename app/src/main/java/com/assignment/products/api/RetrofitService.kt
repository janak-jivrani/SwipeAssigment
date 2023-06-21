package com.assignment.products.api

import com.assignment.products.models.AddProductResponse
import com.assignment.products.models.ProductsListData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.util.ArrayList

interface RetrofitService {

    @Headers("Content-Type: application/json")
    @GET("/api/public/get")
    suspend fun getProducts(): Response<ArrayList<ProductsListData?>?>

    @Multipart
    @POST("api/public/add")
    suspend fun addProduct(
        @PartMap hashMap: HashMap<String, RequestBody>, @Part file: MultipartBody.Part?,
    ): Response<AddProductResponse>
}