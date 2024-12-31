package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Login_(navController: NavController, modifier: Modifier = Modifier) {
    // TODO: switch to rememberSaveable {}
    var un by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(android.graphics.Color.rgb(123, 60, 208)))
            .fillMaxSize()
    ) {
        Text(
            text = "Log in to your account",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = modifier.padding(5.dp)
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = Icons.Default.AccountCircle.name,
                tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = modifier
                    .size(40.dp)
                    .padding(end = 2.dp)
            )
            TextField(
                value = un,
                onValueChange = { input ->
                    un = input
                },
                label = { Text("Username") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = Icons.Default.Lock.name,
                tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = modifier
                    .size(40.dp)
                    .padding(end = 2.dp)
            )
            TextField(
                value = pw,
                onValueChange = { input ->
                    pw = input
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
            )
        }
        Button(
            onClick = {
                // TODO: Auth. call to the backend then if successful navigate, and set the user globally and persistently
                if (un.isNotBlank() && pw.isNotBlank()) {
                    navController.navigate(Routes.RDR)
                } else {
                    showAlert = true
                }
            },
            modifier = modifier.padding(5.dp)
        ) {
            Text(text = "Log in", fontSize = 20.sp)
        }
    }
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Cannot submit") },
            text = { Text("Please fill in both username and password") },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text("OK")
                }
            }
        )
    }
}