package com.example.lap_320.atry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;

public class query extends AppCompatActivity {

    public final static String TAG="Err" ;
    public static  String Name  ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        String url = mwmsession.activemwmurl ;
        String cookies = CookieManager.getInstance().getCookie(url);
        Log.i(TAG, "All the cookies in " + url +" are: " + cookies);

        Button button  = (Button)findViewById(R.id.sbutton) ;
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText)findViewById(R.id.sfield) ;
                Name =  et.getText().toString() ;
                Intent myIntent = new Intent(query.this, activeornot.class) ;
                startActivity(myIntent);
            }
        }) ;
    }

    // Back Pressed is Start Page
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(query.this,display.class);
        startActivity(intent);
    }
}