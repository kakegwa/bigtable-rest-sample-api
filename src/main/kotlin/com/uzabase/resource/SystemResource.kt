package com.uzabase.resource

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/v1/systems")
class SystemResource {

    @GET
    @Path("/ping")
    fun ping(): String {
        return "pong"
    }
}