package com.wunder.test.wunderapp.service.base;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class HttpService {
    public static void post(final Context context, final String url, final ResponseListener listener) {
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                });

        NetworkManager.getInstance(context).addToRequestQueue(request);
    }
}
