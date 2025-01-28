package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Log(navController: NavController, modifier: Modifier = Modifier) {
    val currentUser by UserStore.currentUser.collectAsState()
    var popUpText by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Entry Logs",
            fontSize = 40.sp,
            textAlign = TextAlign.Left,
            color = Color.White,
            modifier = Modifier
                .background(Color(android.graphics.Color.rgb(123, 60, 208)))
                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        )
        // Pull to refresh created following the tutorial found at
        // https://medium.com/@domen.lanisnik/pull-to-refresh-with-compose-material-3-26b37dbea966#:~:text=Adding%20pull%2Dto%2Drefresh%20to,and%20to%20trigger%20data%20refresh.
        // and documentation found at:
        // https://developer.android.com/reference/kotlin/androidx/compose/material3/pulltorefresh/package-summary#PullToRefreshBox(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.pulltorefresh.PullToRefreshState,androidx.compose.ui.Alignment,kotlin.Function1,kotlin.Function1)
        PullToRefreshBox(
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    try {
                        // run auth check to refresh user store and ensure if
                        // user isn't authorised, they are redirected to login page
                        if (!KtorClient.checkAuthStat()) {
                            navController.navigate(Routes.LGN)
                        }
                    } catch (e: Exception) {
                        popUpText = "${e.message}"
                        showAlert = true
                    } finally {
                        isRefreshing = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                if ((currentUser?.logs ?: listOf()).isNotEmpty()) {
                    items(currentUser!!.logs) { currentLog ->
                        Column (Modifier.padding(10.dp)){
                            if (currentLog.perm_granted) {
                                Text(text = currentLog.door_desc + " (Door ID: ${currentLog.door_id})")
                                Text(text = currentLog.accessed, fontStyle = FontStyle.Italic)
                            } else {
                                Text(text = "ACCESS DENIED", color = Color.Red, textDecoration = TextDecoration.Underline)
                                Text(text = currentLog.door_desc + " (Door ID: ${currentLog.door_id})",
                                    color = Color.Red)
                                Text(text = currentLog.accessed, fontStyle = FontStyle.Italic, color = Color.Red)
                            }
                        }
                        HorizontalDivider()
                    }
                } else {
                    items(count = 1, key = null) {
                        Text(
                            text = "No logs to display",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        )
                    }
                }
            }
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
    AlertPopup(showAlert, "Access log fetching failed", popUpText, onDismiss = {popUpText = ""; showAlert = false })
}