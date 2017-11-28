# MarqueeTextNews

## Demo
![result](https://github.com/meny1122/MarqueeTextNews/blob/media/marqueesample.gif)

## Usage
#### XML
    <meny1122.com.marqueetextnews.MarqueeTextNews
        android:id="@+id/marqueeScrollView"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        app:textHeight="30">
    </meny1122.com.marqueetextnews.MarqueeTextNews>

#### CODE
      MarqueeTextNews news = (MarqueeTextNews) findViewById(R.id.marqueeScrollView);
        news.setText("Marquee Text News！！！！！！");
        news.setUrlString("https://github.com/meny1122/MarqueeTextNews");
        news.setTextColor(Color.RED);
        news.startMarquee();

## Inatall
     repositories {
        maven { url 'https://meny1122.github.io/MarqueeTextNews/repository' }
    }

     dependencies {
        compile 'com.meny1122:MarqueeTextNews:1.1'
    }
