package com.uzabase.repository

import com.google.cloud.bigtable.data.v2.BigtableDataClient
import com.google.cloud.bigtable.data.v2.BigtableDataSettings
import com.google.cloud.bigtable.data.v2.models.Query
import com.google.cloud.bigtable.data.v2.models.RowMutation
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CompanyStockRepository {

    companion object {
        const val TABLE_ID = "company_stock"

        private val client = BigtableDataSettings.newBuilderForEmulator("localhost", 8086)
            .setProjectId("dummy")
            .setInstanceId("dummy")
            .build()
            .let { BigtableDataClient.create(it) }
    }

    fun read(key: String): List<Row> {
        val query = Query.create(TABLE_ID).prefix(key)
        val rows = client.readRows(query)
        return rows.map { row ->
            Row(
                row.key.toStringUtf8(),
                row.cells.map { cell -> Cell(cell.family, cell.qualifier.toStringUtf8(), cell.value.toStringUtf8()) }
            )
        }
    }

    fun insert(row: Row) {
        val rowMutation = RowMutation.create(TABLE_ID, row.rowKey)
        row.cells.forEach { rowMutation.setCell(it.columnFamily, it.column, it.value) }
        client.mutateRow(rowMutation)
    }

}

data class Row(val rowKey: String, val cells: List<Cell>)
data class Cell(val columnFamily: String, val column: String, val value: String)
