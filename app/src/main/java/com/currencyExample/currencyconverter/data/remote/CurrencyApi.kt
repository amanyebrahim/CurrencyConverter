package com.currencyExample.currencyconverter.data.remote

import com.currencyExample.currencyconverter.BuildConfig
import com.currencyExample.currencyconverter.domain.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest?access_key=$KEY")
    suspend fun getRates(
        @Query("symbols") base: String
    ): Response<CurrencyResponse>

    companion object {
        const val KEY = BuildConfig.API_KEY
    }
}