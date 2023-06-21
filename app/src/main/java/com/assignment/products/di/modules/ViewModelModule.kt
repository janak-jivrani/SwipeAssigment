package com.assignment.products.di.modules

import com.assignment.products.repositories.ProductsRepository
import com.assignment.products.viewmodels.ProductsViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideProductsViewModel(productsRepository: ProductsRepository): ProductsViewModel {
        return ProductsViewModel(productsRepository)
    }
}