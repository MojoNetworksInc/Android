package com.example.lap_320.atry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class afterlogin extends AppCompatActivity {

    public final static String TAG="Err" ;
    static ArrayList<String> Servers = new ArrayList<String>();
    static ArrayList<String> Urlofservers = new ArrayList<String>();
    private static  String Cookiestring ;
    private static String queryURL ;

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
    public void httpQuery(final ServerCallback callback)
    {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,queryURL,
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
                headers.put("Cookie", Cookiestring);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }


    // OverRiding onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String dname = extras.getString("site");
            String cookie_name = "ATN_CLOUD_DASHBOARD";
            String val = getCookie(dname, cookie_name);
            Cookiestring = cookie_name + "=" + val;
            queryURL = dname + "rest/api/v2/services?type=amc";
            httpQuery(new ServerCallback()
                      {
                          @Override
                          public void onSuccess(JSONObject response)
                          {
                              try
                              {
                                  Servers.clear();
                                  Urlofservers.clear();
                                  JSONObject tmp = response.getJSONObject("data") ;
                                  int totalcount = (int) tmp.get("totalCount") ;
                                  JSONArray services = tmp.getJSONArray("customerServices");


                                  for(int i=0;i<totalcount;i++){
                                      tmp = services.getJSONObject(i) ;
                                      Servers.add((String)tmp.get("customer_service_name")) ;
                                      tmp = tmp.getJSONObject("service") ;
                                      Urlofservers.add((String)tmp.get("service_url")) ;
                                  }
                                  Intent intent1 = new Intent(afterlogin.this,display.class);
                                  startActivity(intent1);
                              }
                              catch (JSONException e)
                              {
                                  Log.e(TAG, "Unexpected JSON exception",e);
                              }
                          }
                      }
            );
        }
    }

    // Back Pressed is Start Page
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(afterlogin.this,MainActivity.class);
        startActivity(intent);
    }

}