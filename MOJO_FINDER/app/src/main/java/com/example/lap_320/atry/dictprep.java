package com.example.lap_320.atry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
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

import java.util.HashMap;
import java.util.Map;

public class dictprep extends AppCompatActivity {

    public final static String TAG="Err" ;
    public static String Cookiestringmwm ;
    public static SparseArray <String> dict ;


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
        setContentView(R.layout.activity_dictprep);

        String queryURLmwm ;
        String dname = mwmsession.activemwmurl ;
        String cookie_name = "JSESSIONID";
        String val = getCookie(dname, cookie_name);
        Cookiestringmwm = cookie_name + "=" + val;


        queryURLmwm = afterlogin.Urlofservers.get(display.activemwm) ;
        queryURLmwm = queryURLmwm + "/new/webservice/v4/locations/tree" ;

        httpQuery(new afterlogin.ServerCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                try
                {
                    JSONArray li = response.getJSONArray("allLocationIds") ;
                    int len = li.length() ;
                    JSONObject tmp ;
                    dict = new SparseArray<String>();
                    for(int i =0;i<len;i++)
                    {
                        tmp = li.getJSONObject(i) ;
                        int key = (int)tmp.get("id") ;
                        if(key!=-1) {
                            dict.put(key, "");
                        }
                    }
                    len = dict.size() ;
                    Log.i(TAG,"Length of List "+String.valueOf(len)) ;
                    JSONObject loc = response.getJSONObject("locations") ;
                    for(int i=0;i<len;i++)
                    {
                        String qu = "{\"type\":\"locallocationid\",\"id\":" + String.valueOf(dict.keyAt(i)) + "}" ;
                        JSONObject iloc  = loc.getJSONObject(qu) ;
                        String name  = (String)iloc.get("name") ;
                        dict.put(dict.keyAt(i),name);
                        Log.i(TAG,name) ;
                    }
                    Log.i(TAG,String.valueOf(dict.size())) ;
                    Intent intent1 = new Intent(dictprep.this,query.class);
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