package www.youtube.The_Break_Free_Program;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebView1 extends AppCompatActivity {

    private WebView web9;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_donate);

        web9 = (WebView) findViewById(R.id.web9);
        web9.getSettings().getJavaScriptEnabled();
        web9.loadUrl("https://www.youtube.com/watch?v=MV9H6dIWohk&list=RDsZYgsZPAPSs&index=3");

    }


}
