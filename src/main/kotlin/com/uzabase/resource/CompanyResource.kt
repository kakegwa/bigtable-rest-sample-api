package com.uzabase.resource

import com.uzabase.domain.*
import com.uzabase.service.CompanyStockService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestPath
import java.time.LocalDate

@Path("/v1/companies")
class CompanyResource(private val service: CompanyStockService) {

    @GET
    @Path("/{companyId}/stocks")
    @Produces(MediaType.APPLICATION_JSON)
    fun get(companyId: String): Response? {
        return service.get(CompanyId(companyId))
            .map { StockJson(it.date.toString(), it.price.openPrice, it.price.closingPrice, it.currency.value) }
            .let { StockResultsJson(it) }
            .let { Response.ok(it).build() }
    }

    @POST
    @Path("/{companyId}/stocks")
    @Consumes(MediaType.APPLICATION_JSON)
    fun store(@RestPath companyId: String, json: StockJson): Response? {
        service.store(
            CompanyId(companyId),
            StockPrice(Price(json.openPrice, json.closingPrice), Currency(json.currency), LocalDate.parse(json.date))
        )
        return Response.status(201).build()
    }
}

data class StockJson(val date: String, val openPrice: Int, val closingPrice: Int, val currency: String)
data class StockResultsJson(val results: List<StockJson>)
