package com.example.lap_320.atry;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build.*;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class display extends AppCompatActivity {

    public final static String TAG="Err" ;
    public static int activemwm ;
    private LinearLayout layout ;
    private int aiwiee  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final ArrayList<String> Names = afterlogin.Servers ;
        final ArrayList<String> Urls = afterlogin.Urlofservers ;

        TextView tv  = new TextView(this) ;
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText("");
        layout.addView(tv);

        TextView tv1  = new TextView(this) ;
        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv1.setText("");
        layout.addView(tv1);

        TextView tv2  = new TextView(this) ;
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv2.setText("Available MWM Servers:");
        tv2.setTypeface(null,Typeface.BOLD);
        layout.addView(tv2);

        TextView tv3  = new TextView(this) ;
        tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv3.setText("");
        layout.addView(tv3);

        TextView tv4  = new TextView(this) ;
        tv4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv4.setText("");
        layout.addView(tv4);

        int len  ;
        len  = Names.size() ;
        TextView[] tvarray = new TextView[len];
        for(int i=0;i<len;i++)
        {
            tvarray[i] = new TextView(this);
            tvarray[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvarray[i].setText(Names.get(i));
            tvarray[i].setId(i);
            aiwiee = i ;
            tvarray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent2 = new Intent(display.this, mwmsession.class) ;
                    display.activemwm = v.getId() ;
                    startActivity(myIntent2);
                }
            });
            layout.addView(tvarray[i]);
        }

        TextView tv5  = new TextView(this) ;
        tv5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv5.setText("");
        layout.addView(tv5);

        TextView tv6  = new TextView(this) ;
        tv6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv6.setText("");
        layout.addView(tv6) ;



        // Log Out Text View
        TextView tvarray7 = new TextView(this);
        tvarray7.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvarray7.setText("Logout");
        tvarray7.setTypeface(null,Typeface.BOLD);
        tvarray7.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager.getInstance().flush();
                } else
                {
                    CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(display.this);
                    cookieSyncMngr.startSync();
                    CookieManager cookieManager=CookieManager.getInstance();
                    cookieManager.removeAllCookie();
                    cookieManager.removeSessionCookie();
                    cookieSyncMngr.stopSync();
                    cookieSyncMngr.sync();
                }
                Intent myIntent3 = new Intent(display.this, MainActivity.class) ;
                startActivity(myIntent3);
                finish() ;
            }
        });
        layout.addView(tvarray7);
        // Logout View End.

        setContentView(layout);
    }

    // Back Pressed is Start Page
    @Override
    public void onBackPressed() {
    }
}