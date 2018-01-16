package com.example.lap_320.atry;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class mwmsession extends AppCompatActivity {

    public final static String TAG="Err" ;
    private WebView wv2;
    public static String activemwmurl ;
    private int count ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mwmsession);

        wv2 = (WebView)findViewById(R.id.getmwmsession) ;
        final String mwmurl = afterlogin.Urlofservers.get(display.activemwm) ;
        count=0 ;

        wv2.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                count=count+1 ;
                if(count<2) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.i(TAG, "All the cookies in " + url + " are: " + cookies);
                    activemwmurl = url;
                    Intent intent = new Intent(mwmsession.this, dictprep.class);
                    startActivity(intent);
                }
            }
        });
        wv2.loadUrl(mwmurl);
    }
}
