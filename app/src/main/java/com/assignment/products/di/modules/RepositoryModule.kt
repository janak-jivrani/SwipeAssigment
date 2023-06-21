package com.assignment.products.di.modules

import com.assignment.products.api.RetrofitService
import com.assignment.products.repositories.ProductsRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideProductsRepository(retrofitService: RetrofitService): ProductsRepository {
        return ProductsRepository(retrofitService)
    }
}