package meny1122.com.marqueetextnews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * Created by yuyamatsushima on 2017/09/10.
 */


class NetworkReceiver(private val mObserver: Observer) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        if (info == null) {
            mObserver.onDisconnect()
        } else {
            mObserver.onConnect()
        }
    }

    interface Observer {
        fun onConnect()
        fun onDisconnect()
    }
}