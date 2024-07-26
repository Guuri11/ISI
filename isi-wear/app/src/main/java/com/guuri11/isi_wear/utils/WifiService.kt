package com.guuri11.isi_wear.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.os.Handler
import android.os.Looper
import android.util.Log

class WifiService {
    fun isConnectedToWifi(context: Context, ssid: String, callback: (Boolean) -> Unit) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, capabilities)
                val wifiInfo = capabilities.transportInfo as? WifiInfo
                wifiInfo?.let {
                    Log.d("WifiService", "SSID: ${it.ssid}, Expected SSID: $ssid")
                    val isConnected = it.ssid == ssid
                    Log.d("WifiService", if (isConnected) "CONNECTED TO WIFI" else "NOT CONNECTED")
                    callback(isConnected)
                } ?: run {
                    Log.d("WifiService", "Could not retrieve WifiInfo")
                    callback(false)
                }
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val wifiInfo = capabilities?.transportInfo as? WifiInfo
                val isConnected = wifiInfo?.ssid == ssid
                callback(isConnected)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Optional: Unregister the network callback after a delay or when no longer needed
        Handler(Looper.getMainLooper()).postDelayed({
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }, 10000) // Unregister after 10 seconds (adjust as needed)
    }
}
