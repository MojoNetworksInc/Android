package com.example.lap_320.atry;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class activeornot extends AppCompatActivity {

    public final static String TAG="Err" ;
    public static String Cookiestringmwm ;
    public static int totalcount  ;



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
        setContentView(R.layout.activity_activeornot);

        String queryURLmwm ;
        String dname = mwmsession.activemwmurl ;
        String cookie_name = "JSESSIONID";
        String val = getCookie(dname, cookie_name);
        Cookiestringmwm = cookie_name + "=" + val;
        Log.i(TAG, "activeornot");
        Log.i(TAG, "Site= " + dname);
        Log.i(TAG, "CookieString= " + Cookiestringmwm);

        queryURLmwm = afterlogin.Urlofservers.get(display.activemwm) ;
        String sname = query.Name ;
        String filter_val = "{\"property\":null,\"value\":[{\"property\":\"macaddress\",\"value\":[\"" + sname + "\"],\"operator\":\"=\"},{\"property\":\"username\",\"value\":[\"" + sname + "\"],\"operator\":\"contains\"},{\"property\":\"devicename\",\"value\": [\"" + sname + "\"],\"operator\":\"contains\"}],\"operator\":\"OR\"}";
        try {
            filter_val = URLEncoder.encode(filter_val, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queryURLmwm = queryURLmwm + "/new/webservice/v4/devices/clients/0/0?filter=" ;
        queryURLmwm = queryURLmwm + filter_val ;
        Log.i(TAG,queryURLmwm) ;

        httpQuery(new afterlogin.ServerCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                try
                {
                    totalcount = (int) response.get("totalCount") ;
                    Log.i(TAG,"Total= " + String.valueOf(totalcount)) ;
                    Intent intent1 = new Intent(activeornot.this,activeornot1.class);
                    startActivity(intent1);
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Unexpected JSON exception",e);
                }
            }
        } , queryURLmwm);
    }
}
