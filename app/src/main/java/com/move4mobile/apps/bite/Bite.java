package com.move4mobile.apps.bite;

/**
 * Created by casvd on 8-11-2016.
 */
public class Bite {
    private String opened_by;
    private String store;
    private long open_time;
    private long close_time;

    public Bite() {
    }

    public Bite(String opened_by, String store, long open_time, long close_time) {
        this.opened_by = opened_by;
        this.store = store;
        this.open_time = open_time;
        this.close_time = close_time;
    }

    public String getOpened_by() {
        return opened_by;
    }

    public String getStore() {
        return store;
    }

    public long getOpen_time() {
        return open_time;
    }

    public long getClose_time() {
        return close_time;
    }
}
