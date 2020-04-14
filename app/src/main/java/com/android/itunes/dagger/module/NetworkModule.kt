package com.android.itunes.dagger.module

import com.android.itunes.api.ApiRepository
import com.android.itunes.api.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val ITUNES_BASE_URL = "https://itunes.apple.com/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val newBuilder = httpBuilder.build().newBuilder().addInterceptor(httpLoggingInterceptor)
        return newBuilder.build()
    }

    @Singleton
    @Provides
    fun providesRetrofitService(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ITUNES_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): ApiService {
        return retrofit.create<ApiService>(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepository(apiService: ApiService): ApiRepository {
        return ApiRepository(apiService)
    }
}