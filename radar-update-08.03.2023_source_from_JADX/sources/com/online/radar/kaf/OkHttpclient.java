package com.online.radar.kaf;

import okhttp3.OkHttpClient;

public class OkHttpclient {
    private static OkHttpclient singletonInstance;
    private OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();

    private OkHttpclient() {
    }

    public static OkHttpclient getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new OkHttpclient();
        }
        return singletonInstance;
    }

    public OkHttpClient getClient() {
        return this.client;
    }

    public void closeConnections() {
        this.client.dispatcher().cancelAll();
    }
}
