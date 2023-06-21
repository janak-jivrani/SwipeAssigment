package com.assignment.products.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.products.models.AddProductRequest
import com.assignment.products.models.AddProductResponse
import com.assignment.products.models.ProductsListData
import com.assignment.products.repositories.ProductsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class ProductsViewModel(private val repo: ProductsRepository) : ViewModel() {
    val liveDataObserver = MutableLiveData<ProductsState>()

    fun getProductsList() {
        liveDataObserver.value = ProductsState.Loading(true)
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val res = repo.getProducts()
            withContext(Dispatchers.Main) {
                liveDataObserver.value = ProductsState.Loading(false)
                if (res.isSuccessful) {
                    liveDataObserver.value = ProductsState.Success(res.body())
                } else {
                    onError("Error : ${res.message()} ")
                }
            }
        }
    }

    fun addProduct(addProductReq: AddProductRequest) {
        liveDataObserver.value = ProductsState.Loading(true)
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val res = repo.addProduct(addProductReq)
            withContext(Dispatchers.Main) {
                liveDataObserver.postValue(ProductsState.Loading(false))
                if (res.isSuccessful && res.body()?.success!!) {
                    liveDataObserver.value = ProductsState.SuccessAddProduct(res.body())
                } else if (!res.body()?.success!!) {
                    onError("Error : ${res.body()?.message} ")
                } else {
                    onError("Error : ${res.message()} ")
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        liveDataObserver.postValue(ProductsState.Loading(false))
        liveDataObserver.postValue(ProductsState.Error(message))
    }
}

sealed class ProductsState {
    data class Loading(val isLoading: Boolean) : ProductsState()
    data class Success(val productsList: ArrayList<ProductsListData?>?) : ProductsState()
    data class SuccessAddProduct(val productRes: AddProductResponse?) : ProductsState()
    data class Error(val error: String) : ProductsState()
}