package meny1122.com.marqueetextnewssample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import meny1122.com.marqueetextnews.MarqueeTextNews

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val marquee: MarqueeTextNews = findViewById(R.id.marqueeScrollView)
        marquee.apply {
            setText("Marquee Text News！！！！")
            setUrlString("https://github.com/meny1122/MarqueeTextNews")
            setTextColor(Color.BLACK)
            startMarquee()
        }
    }
}