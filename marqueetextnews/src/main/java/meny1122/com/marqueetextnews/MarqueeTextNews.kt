package meny1122.com.marqueetextnews

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*


class MarqueeTextNews @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr), NetworkReceiver.Observer, DefaultLifecycleObserver {

    private val marqueeText: TextView
    private var marqueeString = ""
    private var urlString = ""
    private var textHeight = 10
    private var textColor = Color.rgb(145, 145, 145)
    private var isScrolling = false
    private var stringWidth = 0
    private var viewWidth = 0
    private var scrollJob: Job? = null
    private var networkReceiver: NetworkReceiver? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        marqueeText = initMarqueeTextView(attrs)
        setupLifecycleObserver()
        setNetworkReceiver()
    }

    private fun setupLifecycleObserver() {
        if (context is LifecycleOwner) {
            (context as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope.cancel()
        unregisterNetworkReceiver()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        coroutineScope.cancel()
        unregisterNetworkReceiver()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean = true

    private fun initMarqueeTextView(attrs: AttributeSet?): TextView {
        return TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER_VERTICAL
            setOnClickListener {
                if (Utils.isConnected(context) && urlString.isNotEmpty()) {
                    openURL()
                }
            }
            setTextHeight(attrs)
            addView(this)
        }
    }

    private fun startScrolling() {
        scrollJob = coroutineScope.launch {
            var startX = 0
            val sleep = Utils.convertPx2Dp(14, context).toLong()

            while (isActive && isScrolling) {
                val limit = (viewWidth - stringWidth) / 2 + stringWidth
                startX += 1
                scrollTo(startX, 0)
                if (startX >= limit) {
                    startX = 0
                }
                delay(sleep)
            }
        }
    }

    private fun resizeTextSize() {
        val height = textHeight - 4
        var textSize = 0f
        val paint = Paint()

        while (true) {
            paint.textSize = textSize
            val fontMetrics = paint.fontMetrics
            val textHeight = Math.abs(fontMetrics.top) + Math.abs(fontMetrics.descent)
            if (height < textHeight) break
            textSize++
        }

        marqueeText.textSize = textSize
    }

    private fun resizeMarqueeView() {
        val paint = Paint().apply { textSize = marqueeText.textSize }
        stringWidth = paint.measureText(marqueeText.text.toString()).toInt()
        viewWidth = 0

        while (viewWidth < Utils.deviceWidth(context) * 2 + stringWidth) {
            marqueeText.text = "　${marqueeText.text}　"
            viewWidth = paint.measureText(marqueeText.text.toString()).toInt()
        }
        marqueeText.width = viewWidth
    }

    private fun setTextHeight(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextNews).apply {
            textHeight = getInteger(R.styleable.MarqueeTextNews_textHeight, 10)
            recycle()
        }
    }

    private fun setNetworkReceiver() {
        networkReceiver = NetworkReceiver(this).also { receiver ->
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(receiver, filter)
        }
    }

    private fun unregisterNetworkReceiver() {
        networkReceiver?.let {
            context.unregisterReceiver(it)
            networkReceiver = null
        }
    }

    override fun onDisconnect() {
        setOffLineText()
    }

    override fun onConnect() {
        setText(marqueeString)
        startMarquee()
    }

    private fun openURL() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        context.startActivity(intent)
    }

    private fun setOffLineText() {
        stopMarquee()
        post {
            scrollTo(0, 0)
            marqueeText.apply {
                text = context.getString(R.string.off_line)
                resizeTextSize()
                width = Utils.deviceWidth(context)
                gravity = Gravity.END
                setTextColor(Color.rgb(145, 145, 145))
            }
        }
    }

    fun setText(text: String) {
        scrollTo(0, 0)
        marqueeString = text
        marqueeText.apply {
            setTextColor(textColor)
            this.text = marqueeString
        }
        resizeTextSize()
        resizeMarqueeView()
    }

    fun setUrlString(url: String) {
        urlString = url
    }

    fun setTextColor(color: Int) {
        textColor = color
        marqueeText.setTextColor(textColor)
    }

    fun startMarquee() {
        if (!isScrolling && marqueeString.isNotEmpty()) {
            isScrolling = true
            startScrolling()
        }
    }

    fun stopMarquee() {
        isScrolling = false
        scrollJob?.cancel()
        scrollJob = null
    }
}