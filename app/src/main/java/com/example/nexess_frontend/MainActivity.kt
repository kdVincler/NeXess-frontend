package com.example.nexess_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.example.nexess_frontend.ui.theme.NeXessFrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KtorClient.init(this)
        enableEdgeToEdge()
        setContent {
            NeXessFrontendTheme {
                DisposableEffect(Unit) {
                    onDispose {
                        KtorClient.close()
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavBlock(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
