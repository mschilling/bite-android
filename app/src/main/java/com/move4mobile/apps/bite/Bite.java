package com.move4mobile.apps.bite;

import com.google.firebase.database.PropertyName;

/**
 * Created by casvd on 8-11-2016.
 */
public class Bite {
    @PropertyName("opened_by")
    private String openedBy;

    private String store;

    @PropertyName("open_time")
    private long openTime;

    @PropertyName("close_time")
    private long closeTime;

    public Bite() {
    }

    public Bite(String openedBy, String store, long openTime, long closeTime) {
        this.openedBy = openedBy;
        this.store = store;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    @PropertyName("opened_by")
    public String getOpenedBy() {
        return openedBy;
    }

    public String getStore() {
        return store;
    }

    @PropertyName("open_time")
    public long getOpenTime() {
        return openTime;
    }

    @PropertyName("close_time")
    public long getCloseTime() {
        return closeTime;
    }
}
