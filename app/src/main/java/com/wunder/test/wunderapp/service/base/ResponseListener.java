package com.wunder.test.wunderapp.service.base;

public interface ResponseListener {
    void onError(String message);
    void onResponse(String response);
}
