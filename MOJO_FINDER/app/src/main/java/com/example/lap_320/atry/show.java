package com.example.lap_320.atry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class show extends AppCompatActivity {
    public final static String TAG="Err" ;
    private LinearLayout layout ;
    private ScrollView finallayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        finallayout = new ScrollView(this) ;

        TextView tv  = new TextView(this) ;
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText("");
        layout.addView(tv);

        TextView tv1  = new TextView(this) ;
        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv1.setText("");
        layout.addView(tv1);

        if(activeornot1.flag==0) {

        Intent inn = getIntent();
        ArrayList<String> list1 = inn.getStringArrayListExtra("location");
        ArrayList<String> list2 = inn.getStringArrayListExtra("status");
        ArrayList<String> list3 = inn.getStringArrayListExtra("uname");
        ArrayList<String> list4 = inn.getStringArrayListExtra("dname");
        ArrayList<String> list5 = inn.getStringArrayListExtra("mac");
        ArrayList<String> list6 = inn.getStringArrayListExtra("date");



            int len = list1.size();
            TextView[] tvarray = new TextView[len];
            StringBuilder[] builder = new StringBuilder[len];
            for (int i = 0; i < len; i++) {

                tvarray[i] = new TextView(this);
                builder[i] = new StringBuilder();
                builder[i].append(list1.get(i) + "\n" +
                        list2.get(i) + "\n" +
                        list3.get(i) + "\n" +
                        list4.get(i) + "\n" +
                        list5.get(i) + "\n" +
                        list6.get(i) + "\n\n");
                tvarray[i].setText(builder[i].toString());
                tvarray[i].setGravity(Gravity.CENTER);
                layout.addView(tvarray[i]);
            }
        }
        else
        {
            TextView tv2  = new TextView(this) ;
            tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv2.setText("No User Found");
            layout.addView(tv2);
        }
        finallayout.addView(layout);
        setContentView(finallayout);
    }
    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(show.this, query.class);
        startActivity(intent);
    }
}
