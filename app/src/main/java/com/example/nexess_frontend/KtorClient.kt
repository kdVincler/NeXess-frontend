package com.example.nexess_frontend

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

object KtorClient {
    private val client = HttpClient(CIO)
    private const val API = "http://<IP ADDRESS>:8000" // TODO: CHANGE THIS TO CURRENT IP OF LAPTOP

    suspend fun checkServerHealth(): String{
        val response = client.get("$API/health/") {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
        }
        return  response.body()
    }

    fun close() {
        client.close()
    }
}