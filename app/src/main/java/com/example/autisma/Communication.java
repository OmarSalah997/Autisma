package com.example.autisma;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
    private Context mCtx;
    private RequestQueue requestQueue ;
    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result) throws JSONException;
        void onErrorResponse(VolleyError error);
    }
    Communication(Context context){
        this.mCtx=context;
         this.requestQueue = Volley.newRequestQueue(mCtx);
    }
    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public void REQUEST_NO_AUTHORIZE (int method, String URL, JSONObject jsonBody, final VolleyCallback callback)
    {

        JsonObjectRequest JSONRequest = new JsonObjectRequest(method, URL,jsonBody ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY",response.toString());
                try {
                    callback.onSuccessResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        JSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(JSONRequest);
    }


    public  void REQUEST_AUTHORIZE (final String TOKEN, int method, String URL, JSONObject jsonBody, final VolleyCallback callback)
    {
        JsonObjectRequest stringRequest = new JsonObjectRequest(method, URL,jsonBody ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY",response.toString());
                try {
                    callback.onSuccessResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                int erCode=error.networkResponse.statusCode;


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


        getRequestQueue().add(stringRequest);
    }

}
