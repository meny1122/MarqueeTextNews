package meny1122.com.marqueetextnews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService

class NetworkReceiver(private val observer: Observer) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ConnectivityManager.CONNECTIVITY_ACTION) return

        val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return

        val isConnected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkNetworkCapabilities(connectivityManager)
        } else {
            @Suppress("DEPRECATION")
            checkLegacyNetworkInfo(connectivityManager)
        }

        if (isConnected) {
            observer.onConnect()
        } else {
            observer.onDisconnect()
        }
    }

    @Suppress("DEPRECATION")
    private fun checkLegacyNetworkInfo(connectivityManager: ConnectivityManager): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnected == true
    }

    private fun checkNetworkCapabilities(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    interface Observer {
        fun onConnect()
        fun onDisconnect()
    }

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return false
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.isConnected == true
            }
        }
    }
}