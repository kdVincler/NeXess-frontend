package com.example.nexess_frontend

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.ktor.client.plugins.cookies.CookiesStorage
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
            var cEncoded = secureCookieStore.getStringSet("cookies", emptySet())
            // currently stored decoded cookie set
            val cDecoded = mutableSetOf<Cookie>()
            // decode and add encoded cookies to cDecoded (if there are any stored)
            if (!cEncoded.isNullOrEmpty()) {
                cEncoded.forEach() {
                    cDecoded.add(
                        Json.decodeFromString<Cookie>(it)
                    )
                }
            }
            // if adding a cookie that has been stored before (has same name and domain),
            // delete previous instance and add the new
            cDecoded.removeIf{it.name == cookie.name && it.domain == cookie.domain}
            cDecoded.add(cookie)
            // encode the new set of cookies
            cEncoded = mutableSetOf<String>()
            cDecoded.forEach() {
                cEncoded.add(Json.encodeToString(it))
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
            val cEncoded = secureCookieStore.getStringSet("cookies", emptySet())
            val cDecoded = mutableSetOf<Cookie>()
            // decode and add encoded cookies to cDecoded (if there are any stored)
            if (!cEncoded.isNullOrEmpty()) {
                cEncoded.forEach() {
                    cDecoded.add(
                        Json.decodeFromString<Cookie>(it)
                    )
                }
            }
            // remove expired cookies
            val now = GMTDate()
            // both cookies (csrf and session) that are returned from django have an expiry date,
            // and consequently expires should be set
            cDecoded.removeIf {it.expires!! < now}
            return cDecoded.toList()
        }
    }

    override fun close() {
        // as cookies are stored in shared preferences, there's nothing to clean up on close
    }

}