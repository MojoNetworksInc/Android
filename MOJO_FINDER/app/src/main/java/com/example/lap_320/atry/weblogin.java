package com.example.lap_320.atry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log ;
import android.webkit.CookieManager ;
import android.net.Uri ;
import android.content.Intent ;
import android.annotation.TargetApi ;
import android.os.Build ;


public class weblogin extends AppCompatActivity {

    private WebView wv1;
    private static final String TAG="Err" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weblogin);

        wv1  = (WebView)findViewById(R.id.webv) ;
        wv1.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //url = url.substring(0, url.length() - 1);
                String cookies = CookieManager.getInstance().getCookie(url);
                Log.i(TAG, "All the cookies in " + url +" are: " + cookies);
                if(url.contains("dashboard.mojonetworks.com"))
                {
                    Intent intent = new Intent(weblogin.this,afterlogin.class);
                    intent.putExtra("site", url);
                    startActivity(intent);
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final Uri uri = Uri.parse(url);
                return handleUri(uri);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return handleUri(uri);
            }

            private boolean handleUri(final Uri uri) {
                final String host = uri.getHost();
                return false ;
            }
        });
        wv1.loadUrl("https://alpha-login.mojonetworks.com/cas/login");
    }
}