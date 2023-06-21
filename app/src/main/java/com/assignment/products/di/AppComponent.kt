package com.assignment.products.di

import com.assignment.products.di.modules.ContextModule
import com.assignment.products.di.modules.NetworkModule
import com.assignment.products.di.modules.RepositoryModule
import com.assignment.products.di.modules.ViewModelModule
import com.assignment.products.fragments.AddProductFragment
import com.assignment.products.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ContextModule::class, ViewModelModule::class, RepositoryModule::class, NetworkModule::class]
)
interface AppComponent {
    // Classes that can be injected by this Component
    fun inject(application: ProductsApplication)
    fun inject(homeFragment: HomeFragment)
    fun inject(addProductFragment: AddProductFragment)
}