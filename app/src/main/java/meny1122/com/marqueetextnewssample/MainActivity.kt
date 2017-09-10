package meny1122.com.marqueetextnewssample

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import meny1122.com.marqueetextnews.MarqueeTextNews

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scrollView = findViewById(R.id.marqueeScrollView) as MarqueeTextNews
        scrollView.setText("☆★☆Google☆★☆")
        scrollView.setUrlString("https://www.google.co.jp/")
        scrollView.setTextColor(Color.BLACK)
        scrollView.startMarquee()
    }
}
