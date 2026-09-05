package com.beforezune.dune

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class BackupNetwork(context: Context) {
    private val manager = context.getSystemService(ConnectivityManager::class.java)

    fun isOnline(): Boolean {
        val network = manager.activeNetwork ?: return false
        val caps = manager.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
