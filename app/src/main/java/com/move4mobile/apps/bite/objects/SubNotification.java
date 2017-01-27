package com.move4mobile.apps.bite.objects;

import java.util.Map;

/**
 * Created by casvd on 27-1-2017.
 */

public class SubNotification {
    private String user;
    private String token;
    private boolean subscribe;
    private Map<String, String> timestamp;

    public SubNotification(String user, String token, boolean subscribe, Map<String, String> timestamp) {
        this.user = user;
        this.token = token;
        this.subscribe = subscribe;
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }
}
