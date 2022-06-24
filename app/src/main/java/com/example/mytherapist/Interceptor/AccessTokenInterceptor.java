package com.example.mytherapist.Interceptor;

import android.util.Base64;

import androidx.annotation.NonNull;


;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {

    public AccessTokenInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String keys = "MhXH57VYaeogRgYGRl9seT8pnxBUoTG9"+ ":" + "UXcJsVwR5jxEArWh";

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(request);
    }
}
