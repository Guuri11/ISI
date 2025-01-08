package org.guuri11.isimultiplatform

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class BluetoothReceiver(private val bluetoothViewModel: BluetoothViewModel) : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                device?.let {
                    // TODO: Extract this to settings
                    if (device.name.lowercase().contains("ford fiesta")) {
                        bluetoothViewModel.updateConnectionStatus(true)
                    }
                }
            }

            if (it.action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                bluetoothViewModel.updateConnectionStatus(false)
            }

            if (it.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                if (state == BluetoothAdapter.STATE_OFF) {
                    bluetoothViewModel.updateConnectionStatus(false)
                }
            }
        }
    }
}
