package com.currencyExample.currencyconverter.domain.models


data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)