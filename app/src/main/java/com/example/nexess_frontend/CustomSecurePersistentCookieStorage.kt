package com.example.nexess_frontend

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.fillDefaults
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CustomSecurePersistentCookieStorage(c: Context) : CookiesStorage{
    /*
        written based on the Ktor documentation's AcceptAllCookiesStorage.kt
        (https://ktor.io/docs/client-cookies.html#custom_storage)
        and reworked with the usage principles inferred from Shared Preferences usage tutorial
        (https://www.geeksforgeeks.org/shared-preferences-in-android-with-examples/)
        for EncryptedSharedPreferences
        (https://developer.android.com/reference/kotlin/androidx/security/crypto/EncryptedSharedPreferences)
     */

    private val masterKey: MasterKey = MasterKey.Builder(c)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val secureCookieStore: SharedPreferences = EncryptedSharedPreferences.create(
        c,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // mutual exclusion for synchronisation
    private val mx = Mutex()

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        if (cookie.name.isBlank()) {
            return
        }

        mx.withLock {
            // currently stored (string) encoded cookie set
            var cEncoded = secureCookieStore.getStringSet("cookies", mutableSetOf<String>()) ?: mutableSetOf<String>()
            // currently stored decoded cookie set
            val cDecoded = mutableSetOf<Cookie>()
            // decode and add encoded cookies to cDecoded (if there are any stored)
            cEncoded.forEach {
                cDecoded.add(
                    Json.decodeFromString<Cookie>(it)
                )
            }
            // if adding a cookie that has been stored before (has same name and matches requestUrl - can't use matches
            // as it does not handle ip address domains, and throws exceptions),
            // delete previous instance
            cDecoded.removeAll{it.name == cookie.name && it.domain == requestUrl.host}
            // and add the new instance of existing cookie but only if the new cookie has value,
            // as when a sessionid cookie gets deleted, Ktor handles it as a sessionid cookie with no
            // value, so don't add that. This combined with the previous removeAll statement results in
            // sessionid cookie not being stored
            if (cookie.value.isNotBlank()) {
                cDecoded.add(cookie)
            }
            // encode edited set of cookies
            cEncoded = mutableSetOf<String>()
            cDecoded.forEach {
                cEncoded.add(
                    Json.encodeToString(it.fillDefaults(requestUrl))
                )
            }
            secureCookieStore.edit().putStringSet(
                "cookies",
                    cEncoded
                ).apply()
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        mx.withLock {
            // currently stored (string) encoded cookie set
            val cEncoded = secureCookieStore.getStringSet("cookies", mutableSetOf<String>()) ?: mutableSetOf<String>()
            val cDecoded = mutableSetOf<Cookie>()
            // decode and add encoded cookies to cDecoded (if there are any stored)
            cEncoded.forEach {
                cDecoded.add(
                    Json.decodeFromString<Cookie>(it)
                )
            }
            val now = GMTDate()
            // both cookies (csrf and session) that are returned from django have an expiry date,
            // and consequently expires should be set
            // filter out expired and non requestUrl matching cookies
            val cFiltered = cDecoded.filter {(it.expires != null && now < it.expires!!) && it.domain == requestUrl.host}
            return cFiltered.toList()
        }
    }

    override fun close() {
        // as cookies are stored in shared preferences, there's nothing to clean up on close
    }

}