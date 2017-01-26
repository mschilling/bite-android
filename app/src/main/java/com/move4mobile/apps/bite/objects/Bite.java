package com.move4mobile.apps.bite.objects;

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

    private String status;

    private int duration;

    public Bite() {
    }

    public Bite(String openedBy, String store, String status, long openTime, long closeTime) {
        this.openedBy = openedBy;
        this.store = store;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public Bite(String openedBy, String store, String status, int duration) {
        this.openedBy = openedBy;
        this.store = store;
        this.status = status;
        this.duration = duration;
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

    public String getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }
}
