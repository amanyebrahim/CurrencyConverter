package com.currencyExample.currencyconverter.data.repositery

import com.currencyExample.currencyconverter.domain.models.CurrencyResponse
import com.currencyExample.currencyconverter.presentation.util.Resource

interface MainRepository {

    suspend fun getRates(base: String): Resource<CurrencyResponse>
}