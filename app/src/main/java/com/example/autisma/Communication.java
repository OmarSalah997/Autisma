/*package com.example.test.ui.login;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Communication {
    private static Context mCtx;
    private RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result);
    }

    public void REQUEST_NO_AUTHORIZE (int method, String URL, JSONObject jsonBody, final VolleyCallback callback){

        JsonObjectRequest JSONRequest = new JsonObjectRequest(method, URL,jsonBody ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY",response.toString());
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        requestQueue.add(JSONRequest);
    }


    public  void REQUEST_AUTHORIZE (final String TOKEN, int method, String URL, JSONObject jsonBody, final VolleyCallback callback){
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,jsonBody ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY",response.toString());
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("token", TOKEN);
                return params;
            }
        };


        requestQueue.add(stringRequest);
    }

}*/
