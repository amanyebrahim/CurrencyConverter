package com.currencyExample.currencyconverter.domain.useCase

import com.currencyExample.currencyconverter.data.repositery.MainRepository
import com.currencyExample.currencyconverter.domain.models.Rates
import com.currencyExample.currencyconverter.presentation.util.CurrencyEvent
import com.currencyExample.currencyconverter.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.math.round

class ConvertCurrencyUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ): Flow<CurrencyEvent> = flow {
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null) {
            emit(CurrencyEvent.Failure("Not a valid amount"))
            return@flow
        }
        val symbols:MutableList<String> = mutableListOf()
        symbols.add(fromCurrency)
        symbols.add(toCurrency)
        emit(CurrencyEvent.Loading)
        when (val ratesResponse = repository.getRates(symbols.joinToString())) {
            is Resource.Error ->
                emit(CurrencyEvent.Failure(ratesResponse.message!!))

            is Resource.Success -> {
                ratesResponse.data?.rates?.let {
                    val rate = getRateForCurrency(toCurrency, it)
                    if (rate == null) {
                        emit(CurrencyEvent.Failure("Unexpected error"))
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        emit(
                            CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        ))
                    }
                }
            }
        }
    }
}
private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
    "CAD" -> rates.cAD
    "HKD" -> rates.hKD
    "ISK" -> rates.iSK
    "EUR" -> rates.eUR
    "PHP" -> rates.pHP
    "DKK" -> rates.dKK
    "HUF" -> rates.hUF
    "CZK" -> rates.cZK
    "AUD" -> rates.aUD
    "RON" -> rates.rON
    "SEK" -> rates.sEK
    "IDR" -> rates.iDR
    "INR" -> rates.iNR
    "BRL" -> rates.bRL
    "RUB" -> rates.rUB
    "HRK" -> rates.hRK
    "JPY" -> rates.jPY
    "THB" -> rates.tHB
    "CHF" -> rates.cHF
    "SGD" -> rates.sGD
    "PLN" -> rates.pLN
    "BGN" -> rates.bGN
    "CNY" -> rates.cNY
    "NOK" -> rates.nOK
    "NZD" -> rates.nZD
    "ZAR" -> rates.zAR
    "USD" -> rates.uSD
    "MXN" -> rates.mXN
    "ILS" -> rates.iLS
    "GBP" -> rates.gBP
    "KRW" -> rates.kRW
    "MYR" -> rates.mYR
    else -> null
}
