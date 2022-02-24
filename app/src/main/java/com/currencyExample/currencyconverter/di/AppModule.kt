package com.currencyExample.currencyconverter.di

import com.currencyExample.currencyconverter.data.remote.CurrencyApi
import com.currencyExample.currencyconverter.domain.repositery.DefaultMainRepository
import com.currencyExample.currencyconverter.data.repositery.MainRepository
import com.currencyExample.currencyconverter.domain.useCase.ConvertCurrencyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideCurrencyApi(okHttpClient: OkHttpClient): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CurrencyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = DefaultMainRepository(api)


    @Provides
    @Singleton
    fun provideConvertCurrencyUseCases(repository: MainRepository) : ConvertCurrencyUseCase {
        return ConvertCurrencyUseCase(repository)
    }

}