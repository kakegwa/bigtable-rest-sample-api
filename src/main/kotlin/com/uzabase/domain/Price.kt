package com.uzabase.domain

import java.time.LocalDate


data class StockPrice(val price: Price, val currency: Currency, val date: LocalDate)

data class Price(val openPrice: Int, val closingPrice: Int)
data class Currency(val value: String)
