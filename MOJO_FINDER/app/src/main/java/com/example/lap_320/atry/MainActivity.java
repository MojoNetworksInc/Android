package com.example.lap_320.atry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log ;
import android.content.Intent  ;
import android.view.View ;
import android.widget.Button ;
import android.widget.TextView ;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {

    public static final String TAG="Checkers" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button  = (Button)findViewById(R.id.mojoconnect) ;
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, weblogin.class) ;
                startActivity(myIntent);
            }
        }) ;
    }

    @Override
    public void onBackPressed() {
    }
}