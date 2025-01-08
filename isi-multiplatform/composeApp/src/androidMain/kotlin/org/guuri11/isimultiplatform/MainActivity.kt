package org.guuri11.isimultiplatform

import App
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import platform.AppContext
import platform.IntentSpeechToText

class MainActivity : ComponentActivity() {
    private val bluetoothViewModel: BluetoothViewModel by viewModels()
    private lateinit var bluetoothReceiver: BluetoothReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothReceiver = BluetoothReceiver(bluetoothViewModel)
        val intent = handleIntent(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                ),
                1001 // REQUEST_CODE_BLUETOOTH
            )
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }
        registerReceiver(bluetoothReceiver, filter)

        AppContext.setUp(applicationContext)

        setContent {
            val saveCarCoordinatesState = remember { mutableStateOf(false) }

            bluetoothViewModel.saveCarCoordinates.observe(this) { saveCarCoordinates ->
                saveCarCoordinatesState.value = saveCarCoordinates
            }

            App(intent, saveCarCoordinatesState.value)
        }
    }

    private fun handleIntent(intent: Intent?): IntentSpeechToText {
        intent?.let {
            when (it.action) {
                Intent.ACTION_ASSIST -> {
                    return IntentSpeechToText.ACTION
                }
                Intent.ACTION_VOICE_COMMAND -> {
                    return IntentSpeechToText.ACTION
                }
                else -> {
                    return IntentSpeechToText.NORMAL
                }
            }
        }
        return IntentSpeechToText.NORMAL
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothReceiver)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}