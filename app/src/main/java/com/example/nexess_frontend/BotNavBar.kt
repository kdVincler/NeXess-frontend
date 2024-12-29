package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nexess_frontend.ui.theme.NeXessFrontendTheme

@Composable
fun BotNavBar(navController: NavController, modifier: Modifier = Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(android.graphics.Color.rgb(123, 60, 208)))
    ) {
        Button(
            onClick ={ navController.navigate(Routes.RDR) },
            colors = ButtonDefaults
                .buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = Icons.Default.LocationOn.name
                )
                Text(text = "Reader")
            }
        }
        Button(
            onClick ={ navController.navigate(Routes.LOG) },
            colors = ButtonDefaults
                .buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = Icons.Default.Info.name
                )
                Text(text = "Logs")
            }
        }
        Button(
            onClick ={ navController.navigate(Routes.PRFL) },
            colors = ButtonDefaults
                .buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = Icons.Default.Person.name
                )
                Text(text = "Profile")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BotNavBarPreview() {
    NeXessFrontendTheme {
        BotNavBar(navController = rememberNavController())
    }
}