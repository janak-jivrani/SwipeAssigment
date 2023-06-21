package com.assignment.products.di

import android.app.Application
import com.assignment.products.di.modules.ContextModule

class ProductsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            component = DaggerAppComponent.builder().contextModule(ContextModule(this)).build()
            component.inject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        lateinit var component: AppComponent
    }
}