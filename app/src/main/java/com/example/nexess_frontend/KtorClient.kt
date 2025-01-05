package com.example.nexess_frontend

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

object KtorClient {
    private val client = HttpClient(CIO)
    private const val API = "http://${BuildConfig.IP}:8000"

    suspend fun checkServerHealth(): HttpResponse{
        val response = client.get("$API/health/") {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
        }
        return  response
    }

    fun close() {
        client.close()
    }
}