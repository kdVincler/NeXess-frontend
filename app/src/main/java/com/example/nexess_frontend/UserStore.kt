package com.example.nexess_frontend

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


// This global user store is based on how a view model would be implemented
// detailed in the android code lab linked below.
// I decided to keep much of the code of the would be view model and went with
// a singleton object instead because I was having problems making the view model
// accessible for both composable and object classes like the Ktor client.
// https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#4

object UserStore {
    private val _currentUser = MutableStateFlow<KtorClient.AuthUser?>(null)
    val currentUser: StateFlow<KtorClient.AuthUser?> = _currentUser.asStateFlow()

    fun setUser(user: KtorClient.AuthUser?) {
        _currentUser.value = user
    }

    fun clearUser() {
        _currentUser.value = null
    }
}