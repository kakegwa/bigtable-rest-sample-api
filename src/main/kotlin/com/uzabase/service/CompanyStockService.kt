package com.uzabase.service

import com.uzabase.domain.CompanyId
import com.uzabase.domain.Currency
import com.uzabase.domain.Price
import com.uzabase.domain.StockPrice
import com.uzabase.repository.Cell
import com.uzabase.repository.CompanyStockRepository
import com.uzabase.repository.Row
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class CompanyStockService(private val repository: CompanyStockRepository) {

    companion object {
        const val COLUMN_FAMILY_STOCK_PRICE = "stock_price"
        const val COLUMN_OPEN_PRICE = "open_price"
        const val COLUMN_CLOSING_PRICE = "closing_price"
        const val COLUMN_CURRENCY = "currency"
    }

    fun get(companyId: CompanyId): List<StockPrice> {
        val rows = repository.read(companyId.value)
        return rows.map { row ->
            StockPrice(
                Price(
                    row.cells.first { it.column == COLUMN_OPEN_PRICE }.value.toInt(),
                    row.cells.first { it.column == COLUMN_CLOSING_PRICE }.value.toInt()
                ),
                Currency(row.cells.first { it.column == COLUMN_CURRENCY }.value),
                LocalDate.parse(row.rowKey.split("#")[1])
            )
        }
    }

    fun store(companyId: CompanyId, stockPrice: StockPrice) {
        val rowKey = "${companyId.value}#${stockPrice.date}"
        val cells = listOf(
            Cell(COLUMN_FAMILY_STOCK_PRICE, COLUMN_OPEN_PRICE, stockPrice.price.openPrice.toString()),
            Cell(COLUMN_FAMILY_STOCK_PRICE, COLUMN_CLOSING_PRICE, stockPrice.price.closingPrice.toString()),
            Cell(COLUMN_FAMILY_STOCK_PRICE, COLUMN_CURRENCY, stockPrice.currency.value)
        )
        repository.insert(Row(rowKey, cells))
    }

}
