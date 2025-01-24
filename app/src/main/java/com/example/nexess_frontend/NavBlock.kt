package com.example.nexess_frontend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavBlock(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SPL, builder = {
        composable(Routes.SPL) {
            SplashScreen_(navController = navController, modifier = modifier)
        }
        composable(Routes.LGN) {
            Login_(navController = navController, modifier = modifier)
        }
        composable(Routes.RDR) {
            Reader(navController = navController, modifier = modifier)
        }
        composable(Routes.LOG) {
            Log(navController = navController, modifier = modifier)
        }
        composable(Routes.PRFL) {
            Prof(navController = navController, modifier = modifier)
        }
    })
}