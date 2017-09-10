package meny1122.com.marqueetextnews

import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by yuyamatsushima on 2017/09/10.
 */

class Utils {

    fun convertPx2Dp(px: Int, context: Context): Long {
        val metrics = context.resources.displayMetrics
        return (px / metrics.density).toLong()
    }

    fun deviceWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun isConnected(context: Context): Boolean {
        val connectManger = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectManger.activeNetworkInfo
        return (networkInfo!= null && networkInfo.isConnected)
    }
}