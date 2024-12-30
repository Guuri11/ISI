package com.guuri11.isi_wear.data.service

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log

class WifiService {
    fun isConnectedToWifi(context: Context, ssid: String): Boolean {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val tag = "WifiConnectionCheck"

        if (wifiInfo != null && wifiInfo.ssid != null) {
            val currentSsid = wifiInfo.ssid
            Log.d(tag, "Connected to SSID: $currentSsid")

            if (currentSsid == ssid) {
                Log.i(tag, "Connected to the specified SSID: $ssid")
                return true
            } else {
                Log.w(tag, "Connected to a different SSID: $currentSsid, expected: $ssid")
            }
        } else {
            Log.e(tag, "No Wi-Fi connection found or unable to retrieve SSID.")
        }

        return false
    }
}
