package com.example.lap_320.atry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.LinearLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class activeornot1 extends AppCompatActivity {

    private LinearLayout layout ;
    public final static String TAG="Err" ;
    public String sname  ;
    public String Cookiestringmwm ;
    public static  int totalcount  ;

    public ArrayList <String> answers1 = new ArrayList <>();
    public ArrayList <String> answers2 = new ArrayList <>();
    public ArrayList <String> answers3 = new ArrayList <>();
    public ArrayList <String> answers4 = new ArrayList <>();
    public ArrayList <String> answers5 = new ArrayList <>();
    public ArrayList <String> answers6 = new ArrayList <>();

    public static int flag ;

    public interface ServerCallback{
        void onSuccess(JSONObject result);
    }

    // Extracting Cookie(CookieName) set by siteName
    public String getCookie(String siteName,String CookieName){
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains(CookieName)){
                String[] temp1=ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }

    // Make HTTP GET REQUEST
    public void httpQuery(final afterlogin.ServerCallback callback , String queryURLmwm)
    {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,queryURLmwm,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Cookie", Cookiestringmwm);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activeornot1);

        sname = query.Name ;
        Cookiestringmwm = activeornot.Cookiestringmwm  ;
        totalcount  = activeornot.totalcount ;

        String queryURLmwm ;
        queryURLmwm = afterlogin.Urlofservers.get(display.activemwm) ;
        queryURLmwm = queryURLmwm + "/new/webservice/v4/devices/clients/" ;

        int times = (totalcount + 999) / 1000 ;
        int init ;
        int last=1000  ;
        try {
            String filter_val = "{\"property\":null,\"value\":[{\"property\":\"macaddress\",\"value\":[\"" + sname + "\"],\"operator\":\"=\"},{\"property\":\"username\",\"value\":[\"" + sname + "\"],\"operator\":\"contains\"},{\"property\":\"devicename\",\"value\": [\"" + sname + "\"],\"operator\":\"contains\"}],\"operator\":\"OR\"}";
            filter_val = URLEncoder.encode(filter_val, "UTF-8");
            answers1.clear() ;
            answers2.clear() ;
            answers3.clear() ;
            answers4.clear() ;
            answers5.clear() ;
            answers6.clear() ;

            for (int i = 0; i < times; i++) {
                init = i * 1000;
                if(i+1==times)
                {
                    Log.i(TAG,"First");
                    httpQuery(new afterlogin.ServerCallback()
                    {
                        @Override
                        public void onSuccess(JSONObject response)
                        {
                            try
                            {
                                // Extract all five(DeviceName,UserName,Up/Down Since,MacAddress,LocationId
                                // Create an pentaTuple Object and put that into the answers
                                JSONArray clients = response.getJSONArray("clientList") ;
                                int len  = clients.length() ;
                                Log.i(TAG,"Clientlist= "+String.valueOf(len)) ;

                                for(int j=0;j<len;j++)
                                {
                                    JSONObject sclient = clients.getJSONObject(j) ;
                                    String DName = (String)sclient.get("name") ;
                                    String UName = (String)sclient.get("userName") ;
                                    boolean Stat = (boolean)sclient.get("active") ;
                                    String Status ;
                                    if(Stat)
                                        Status="Active" ;
                                    else
                                        Status="InActive" ;
                                    JSONArray radio = sclient.getJSONArray("radios") ;
                                    String Mac = (String)(radio.getJSONObject(0)).get("macaddress") ;
                                    long upSince = (long)(radio.getJSONObject(0)).get("upSince") ;
                                    long unixTime = System.currentTimeMillis() ;
                                    int lid  = (int) sclient.getJSONObject("locationId").get("id") ;
                                    String location = (dictprep.dict).get(lid) ;
                                    Log.i(TAG,DName) ;
                                    Log.i(TAG,UName) ;
                                    Log.i(TAG,Mac) ;
                                    Log.i(TAG,Status) ;
                                    Log.i(TAG,"Location "+location) ;
                                    if(upSince+604800000>unixTime)
                                    {
                                        Date date = new Date(upSince);
                                        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
                                        String strDate = formatter.format(date);
                                        Log.i(TAG,"Date "+strDate);

                                        answers1.add(Status) ;
                                        answers2.add(location) ;
                                        answers3.add(UName) ;
                                        answers4.add(DName) ;
                                        answers5.add(Mac) ;
                                        answers6.add(strDate) ;

                                    }
                                }

                                // Start show Activity
                                Intent intent1 = new Intent(activeornot1.this,show.class);
                                intent1.putStringArrayListExtra("status", answers1);
                                intent1.putStringArrayListExtra("location", answers2);
                                intent1.putStringArrayListExtra("uname", answers3);
                                intent1.putStringArrayListExtra("dname", answers4);
                                intent1.putStringArrayListExtra("mac", answers5);
                                intent1.putStringArrayListExtra("date", answers6);
                                startActivity(intent1);

                            }
                            catch (JSONException e)
                            {
                                Log.e(TAG, "Unexpected JSON exception",e);
                            }
                        }
                    } , queryURLmwm + String.valueOf(init) + "/" + String.valueOf(last) + "?filter=" + filter_val);
                }
                else
                {
                    Log.i(TAG,"Second");
                    httpQuery(new afterlogin.ServerCallback()
                    {
                        @Override
                        public void onSuccess(JSONObject response)
                        {
                            try
                            {
                                // Extract all five(DeviceName,UserName,Up/Down Since,MacAddress,LocationId
                                // Create an pentaTuple Object and put that into the answers
                                JSONArray clients = response.getJSONArray("clientList") ;
                                int len  = clients.length() ;
                                Log.i(TAG,"Clientlist= "+String.valueOf(len)) ;

                                for(int j=0;j<len;j++)
                                {
                                    JSONObject sclient = clients.getJSONObject(j) ;
                                    String DName = (String)sclient.get("name") ;
                                    String UName = (String)sclient.get("userName") ;
                                    boolean Stat = (boolean)sclient.get("active") ;
                                    String Status ;
                                    if(Stat)
                                        Status="Active" ;
                                    else
                                        Status="InActive" ;
                                    JSONArray radio = sclient.getJSONArray("radios") ;
                                    String Mac = (String)(radio.getJSONObject(0)).get("macaddress") ;
                                    long upSince = (long)(radio.getJSONObject(0)).get("upSince") ;
                                    long unixTime = System.currentTimeMillis() ;
                                    int lid  = (int) sclient.getJSONObject("locationId").get("id") ;
                                    String location = (dictprep.dict).get(lid) ;
                                    Log.i(TAG,DName) ;
                                    Log.i(TAG,UName) ;
                                    Log.i(TAG,Mac) ;
                                    Log.i(TAG,Status) ;
                                    Log.i(TAG,"Location "+location) ;
                                    if(upSince+604800000>unixTime)
                                    {
                                        Date date = new Date(upSince);
                                        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
                                        String strDate = formatter.format(date);
                                        Log.i(TAG,"Date "+strDate);

                                        answers1.add(Status) ;
                                        answers2.add(location) ;
                                        answers3.add(UName) ;
                                        answers4.add(DName) ;
                                        answers5.add(Mac) ;
                                        answers6.add(strDate) ;

                                    }
                                }
                            }
                            catch (JSONException e)
                            {
                                Log.e(TAG, "Unexpected JSON exception",e);
                            }
                        }
                    } , queryURLmwm + String.valueOf(init) + "/" + String.valueOf(last) + "?filter=" + filter_val);
                }
            }
            if(times==0)
            {
                flag=1  ;
                // Start show Activity
                Intent intent1 = new Intent(activeornot1.this,show.class);
                startActivity(intent1);
            }
            else
            {
                flag=0 ;
            }
        }
        catch (UnsupportedEncodingException e) {
            Log.i(TAG,"Error");
            e.printStackTrace();
        }
    }
}
