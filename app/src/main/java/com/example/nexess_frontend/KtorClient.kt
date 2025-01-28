package com.example.nexess_frontend

import android.content.Context
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.cookies
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

object KtorClient {
    private lateinit var client: HttpClient
    private const val API = "http://${BuildConfig.IP}:8000"

    @Serializable
    data class ErrorMsg(val error: String)
    @Serializable
    data class LoginUser(val un: String, val pw: String)
    @Serializable
    data class AuthLog(val uid: Int, val door_id: Int, val door_desc: String, val accessed: String, val perm_granted: Boolean)
    @Serializable
    data class AuthUser(val initials: String, val name: String, val perm: String, val logs: List<AuthLog>)
    @Serializable
    data class AuthStatus(val authenticated: Boolean, val user: AuthUser?)
    @Serializable
    data class Door(val id: Int)

    suspend fun login(un: String, pw: String) {
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
            throw Exception("Exception raised during login. Description: ${b.error} (${response.status.value})")
        }
        // checkAuthStat to update global user
        checkAuthStat()
    }

    suspend fun checkAuthStat(): Boolean {
        val response = client.get("$API/check_auth_status/") {
            headers {
                contentType(ContentType.Application.Json)
            }
        }
        if (response.status.value != 200) {
            throw Exception("Exception raised during authentication status check. (${response.status.value})")
        }
        val status: AuthStatus = response.body()
        if (status.authenticated) {
            UserStore.setUser(status.user)
        } else {
            UserStore.clearUser()
        }
        return status.authenticated
    }

    suspend fun openDoor(id: Int) {
        val csrfToken = client.cookies(API).firstOrNull{it.name == "csrftoken"}?.value ?: ""
        val response = client.post("$API/door-control/") {
            headers {
                append("X-CSRFToken", csrfToken)
            }
            contentType(ContentType.Application.Json)
            setBody(Door(id))
        }
        if (response.status.value != 200) {
            // Because I return 403 status for permission denied door opening requests,
            // I have to call checkAuthStat() here, to ensure the user doesn't have to refresh on the
            // log screen
            checkAuthStat()
            val b: ErrorMsg = response.body()
            throw Exception("${b.error} (${response.status.value} - ${response.status.description})")
        }
        // checkAuthStat to update global user's logs
        checkAuthStat()
    }

    suspend fun logout() {
        val response = client.get("$API/logout/")
        if (response.status.value != 200) {
            throw Exception("Exception raised during logout. (${response.status.value})")
        }
        // checkAuthStat to update global user
        checkAuthStat()
    }

    fun init(context: Context) {
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(HttpCookies) {
                storage = CustomSecurePersistentCookieStorage(context)
            }
        }
    }

    fun close() {
        client.close()
    }
}