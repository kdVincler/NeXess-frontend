package com.example.nexess_frontend

import android.app.Activity
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun Reader(navController: NavController, modifier: Modifier = Modifier) {
    var popUpTitle by remember { mutableStateOf("") }
    var popUpText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var isReaderRunning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val nfcAdptr = remember { NfcAdapter.getDefaultAdapter(context) }

    /*
        For the NFC reader functionality:
            Inspiration taken from / Based on Abdul Basit's NFCReader-KMP project
            found at:
            https://github.com/SEAbdulbasit/NFCReader-KMP.git
     */

    DisposableEffect(Unit) {
        isReaderRunning = true
        val activity = context as Activity

        val myReaderCallback = NfcAdapter.ReaderCallback {
            isLoading = true
            try {
                val ndef = Ndef.get(it)
                ndef?.connect()
                    ?: throw Exception("Incorrect read on NFC tag (Could not connect to tag Ndef). Please scan again.")

                val isKnownTagFormat = (
                    ndef.ndefMessage?.records?.size
                        ?: throw Exception("Incorrect read on NFC tag (Message read as empty). Please scan again.")
                ) == 1
                // if the records' size isn't 1, then this isn't an NFC tag I formatted to use for Door id.
                // if it is, then I can safely get the first (and only) record from the tag.
                if (isKnownTagFormat) {
                        val record = ndef.ndefMessage.records[0]
                        if (record.tnf == NdefRecord.TNF_WELL_KNOWN
                            &&
                            record.type.contentEquals(NdefRecord.RTD_TEXT))
                        {
                            // convert payload to string and remove the " en" prefix the 'NFC Tools' application
                            // writes into the tag payload incorrectly
                        // TODO: possible that switching to a different NFC utility app to write the tags will solve this
                            val payload = String(record.payload).removeRange(0,3)
                            scope.launch {
                                try {
                                    KtorClient.openDoor(payload.toInt())
                                    // if no exception is thrown and subsequently caught, show a
                                    // pop up message to the user about the successful door opening
                                    popUpTitle = "Permission Granted"
                                    popUpText = "Door opened, please proceed."
                                    showAlert = true
                                } catch (e: Exception) {
                                    popUpTitle = "Permission Denied"
                                    popUpText = "${e.message}"
                                    showAlert = true
                                }
                            }
                        }  else {
                            throw Exception("Unsupported record type.")
                        }
                } else {
                    throw Exception("Unknown tag format.")
                }

                ndef.close()
            } catch (e: Exception) {
                popUpTitle = "NFC reading failure"
                popUpText = "${e.message}"
                showAlert = true
            } finally {
                isLoading = false
            }
        }

        // start and continuously run Nfc reader while on this composable
        try {
            nfcAdptr.enableReaderMode(
                activity,
                myReaderCallback,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B
                        or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V,
                null
            )
            if (!nfcAdptr.isEnabled) {
                throw Exception()
            }
        } catch (e: Exception) {
            popUpTitle = "NFC failure"
            popUpText = "Nfc is disabled. Please enable NFC, then restart the application."
            showAlert = true
            isReaderRunning = false
        }

        // Stop reader when navigating away
        onDispose {
            try {
                nfcAdptr.disableReaderMode(activity)
            } catch (e: Exception) {
                popUpTitle = "NFC failure"
                popUpText = "Nfc is disabled. Please enable NFC, then restart the application."
                showAlert = true
            }
            isReaderRunning = false
        }
    }

    Column (
        modifier = modifier
    ) {
        Text(
            text = "Reader",
            fontSize = 40.sp,
            textAlign = TextAlign.Left,
            color = Color.White,
            modifier = Modifier
                .background(Color(android.graphics.Color.rgb(123, 60, 208)))
                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        )
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            Image(
                painter = painterResource(id = R.drawable.nfc),
                contentDescription = "an image of an NFC coil",
                modifier = Modifier
                    .size(200.dp, 200.dp)
            )
            if (isReaderRunning) {
                Text(
                    text = "Searching for Door ID tags...",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                LinearProgressIndicator(
                    color = Color.Gray,
                    trackColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    strokeCap = StrokeCap.Square
                )
            } else {
                Text(
                    text = "Currently not scanning",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
    AlertPopup(showAlert, popUpTitle, popUpText, onDismiss = {
        popUpTitle = ""
        popUpText = ""
        showAlert = false
    })
    LoadingOverlay(isLoading)
}