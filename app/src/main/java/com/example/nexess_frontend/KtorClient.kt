package com.example.nexess_frontend

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.request.accept
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.header.AcceptEncoding
import io.ktor.http.headers
import kotlinx.serialization.Serializable

object KtorClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpCookies) // TODO: make
    }
    private const val API = "http://${BuildConfig.IP}:8000"

    @Serializable
    data class HealthMsg(val message: String)
    @Serializable
    data class ErrorMsg(val error: String)
    @Serializable
    data class LoginUser(val un: String, val pw: String)
    @Serializable
    data class AuthLog(val uid: Int, val doorId: Int, val accessed: String)
    @Serializable
    data class AuthUser(val id: Int, val un: String, val name: String, val perm: Int, val logs: List<AuthLog>)
    @Serializable
    data class AuthStatus(val authenticated: Boolean, val user: AuthUser?)

    suspend fun checkServerHealth(): String{
        val response = client.get("$API/health/") {
            contentType(ContentType.Application.Json)
        }
        if (response.status.value != 200) {
            val b: ErrorMsg = response.body()
            throw Exception("Error checking server health. Desc.: ${b.error} (${response.status.value})")
        }
        val msg: HealthMsg = response.body()
        return  msg.message
    }

    suspend fun login(un: String, pw: String){
        val csrfToken = client.cookies(API).firstOrNull{it.name == "csrftoken"}?.value ?: ""
        val response = client.post("$API/login/") {
            headers {
                append("X-CSRFToken", csrfToken)
            }
            contentType(ContentType.Application.Json)
            setBody(LoginUser(un, pw))
        }
        if (response.status.value != 200) {
            val b: ErrorMsg = response.body()
            throw Exception("Error while logging in. Desc.: ${b.error} (${response.status.value})")
        }
        // checkAuthStat to update global user
        checkAuthStat()
    }

    suspend fun checkAuthStat(): Boolean{
        val response = client.get("$API/check_auth_status/") {
            headers {
                contentType(ContentType.Application.Json)
            }
        }
        if (response.status.value != 200) {
            throw Exception("Error while checking authentication status. (${response.status.value})")
        }
        val status: AuthStatus = response.body()
        println(client.cookies(API))
        if (status.authenticated) {
            // TODO: set user globally to status.user's values
            return true
        } else {
            // TODO: set user globally to null just to be sure
            return false
        }
    }

    suspend fun logout(){
        val response = client.get("$API/logout/")
        if (response.status.value != 200) {
            val b: ErrorMsg = response.body()
            throw Exception("Error while logging out. (${response.status.value})")
        }
        // checkAuthStat unnecessary as LaunchedEffect in Login_ will run and update
        // and if logout is unsuccessful, redirection wont happen so user will stay logged in
        // with their data staying
    }

    fun close() {
        client.close()
    }
}