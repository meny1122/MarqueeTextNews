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

/**
 * Created by yuyamatsushima on 2017/09/10.
 */

class MarqueeTextNews @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : HorizontalScrollView(context, attrs, defStyleAttr), NetworkReceiver.Observer{

    private var marqueeText: TextView? = null
    private var marqueeString = ""
    private var urlString = ""
    private var textHeight = 10
    private var textColor= Color.rgb(145,145,145)
    private var isScrollFinish = true
    private var stringWidth = 0
    private var viewWidth= 0
    private var mThread: Thread? = null

    init {
        initMarqueeTextView(attrs)
        setNetworkReceiver()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean = true


    private fun initMarqueeTextView(attrs: AttributeSet?) {
        marqueeText = TextView(context)
        val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT)
        marqueeText!!.layoutParams= params
        marqueeText!!.gravity= Gravity.CENTER_VERTICAL
        marqueeText!!.setOnClickListener{
            if (Utils().isConnected(context) && !urlString.isEmpty()) {
                openURL()
            }
        }
        setTextHeight(attrs)
        this.addView(marqueeText)
    }

    private val runnable = Runnable {
        var startX = 0
        val sleep = Utils().convertPx2Dp(14, context)

        while (!isScrollFinish) {
            val limit = (viewWidth- stringWidth) /2 + stringWidth
            startX += 1
            scrollTo(startX, 0)
            if (startX >= limit) {
                startX = 0
            }
            try {
                Thread.sleep(sleep)
            } catch (e: InterruptedException) { e.printStackTrace() }
        }
    }

    private fun resizeTextSize () {

        val height = textHeight - 4
        var textSize = 0f

        val paint = Paint()
        paint.textSize= textSize

        var fontMetrics = paint.fontMetrics
        var textHeight = Math.abs(fontMetrics.top) + Math.abs(fontMetrics.descent)

        while ( height >= textHeight) {
            textSize++
            paint.textSize = textSize

            fontMetrics = paint.fontMetrics
            textHeight = Math.abs(fontMetrics.top) + Math.abs(fontMetrics.descent)
        }

        marqueeText!!.textSize = textSize
    }

    private fun resizeMarqueeView() {
        stringWidth = 0
        viewWidth = 0

        val paint = Paint()
        val textSize = marqueeText!!.textSize

        paint.textSize = textSize
        val textWidth = paint.measureText(marqueeText!!.text.toString())
        stringWidth = textWidth.toInt()

        while (viewWidth < Utils().deviceWidth(context)*2 + stringWidth) {
            val temp = marqueeText!!.text.toString()
            marqueeText!!.text = "　$temp　"
            viewWidth = paint.measureText(marqueeText!!.text.toString()).toInt()
        }
        marqueeText!!.width = viewWidth
    }

    private fun setTextHeight (attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.MarqueeTextNews)
        textHeight = typedArray.getInteger(R.styleable.MarqueeTextNews_textHeight,10)
    }

    private fun setNetworkReceiver () {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val receiver = NetworkReceiver(this)
        context.registerReceiver(receiver,filter)
    }

    override fun onDisconnect() {
        setOffLineText()
    }

    override fun onConnect() {
        setText(marqueeString)
        startMarquee()
    }

    private fun openURL() {
        val uri = Uri.parse(urlString)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

    private fun setOffLineText () {
        stopMarquee()
        marqueeText!!.post({
            scrollTo(0,0)
            marqueeText!!.text = context.getString(R.string.off_line)
            resizeTextSize()
            marqueeText!!.width = Utils().deviceWidth(context)
            marqueeText!!.gravity= Gravity.END
            marqueeText!!.setTextColor(Color.rgb(145,145,145))
        })
    }


    fun setText(text: String) {
        scrollTo(0,0)
        marqueeString = text
        marqueeText!!.setTextColor(textColor)
        marqueeText!!.text = marqueeString
        resizeTextSize()
        resizeMarqueeView()
    }

    fun setUrlString(url: String) {
        urlString = url
    }

    fun setTextColor (color:Int) {
        textColor = color
        marqueeText!!.setTextColor(textColor)
    }

    fun startMarquee() {
        if (isScrollFinish&& !marqueeString.isEmpty()) {
            isScrollFinish = false
            mThread = Thread(runnable)
            mThread!!.start()
        }
    }

    fun stopMarquee() {
        if (!isScrollFinish) {
            isScrollFinish = true
            mThread = null
        }
    }

}
