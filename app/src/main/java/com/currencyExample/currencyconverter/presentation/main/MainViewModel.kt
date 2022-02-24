package com.currencyExample.currencyconverter.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyExample.currencyconverter.domain.useCase.ConvertCurrencyUseCase
import com.currencyExample.currencyconverter.presentation.util.CurrencyEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val useCase: ConvertCurrencyUseCase
): ViewModel() {

    private var _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    var conversion: StateFlow<CurrencyEvent> = _conversion


    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase(amountStr, fromCurrency, toCurrency).onEach { result ->
                when (result) {
                    is CurrencyEvent.Success -> {
                        _conversion.value = CurrencyEvent.Success(
                         result.resultText
                     )
                    }
                    is CurrencyEvent.Failure -> {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    }
                    is CurrencyEvent.Loading -> {
                        _conversion.value = CurrencyEvent.Loading
                    }
                    else -> Unit
                }
            }.launchIn(viewModelScope)

        }
    }
}