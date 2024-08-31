package meny1122.com.marqueetextnews

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.getSystemService

object Utils {

    fun convertPx2Dp(px: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return (px / metrics.density).toInt()
    }

    fun deviceWidth(context: Context): Int {
        val windowManager = context.getSystemService<WindowManager>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager?.currentWindowMetrics
            metrics?.bounds?.width() ?: 0
        } else {
            @Suppress("DEPRECATION")
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager?.activeNetworkInfo
            @Suppress("DEPRECATION")
            return networkInfo?.isConnected == true
        }
    }
}