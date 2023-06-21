package com.assignment.products.di.modules

import com.assignment.products.api.RetrofitService
import com.assignment.products.core.Constant
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    @Provides
    fun provideRetrofitService(okHttpClient: OkHttpClient): RetrofitService {
        val retrofit = Retrofit.Builder().baseUrl(Constant.BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(RetrofitService::class.java)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }
}