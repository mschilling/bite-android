package com.move4mobile.apps.bite.objects;

import com.google.firebase.database.PropertyName;

import java.util.Map;

/**
 * Created by casvd on 28-1-2017.
 */

public class ArchiveUserOrder {
    private String store;
    private String location;

    @PropertyName("open_time")
    private long openTime;

    @PropertyName("close_time")
    private long closeTime;

    private Map<String, Map<String, Object>> products;

    @PropertyName("category")
    private Map<String, Map<String, String>> categories;

    public ArchiveUserOrder() {

    }

    public ArchiveUserOrder(String store, String location, long openTime, long closeTime, Map<String, Map<String, Object>> products, Map<String, Map<String, String>> categories) {
        this.store = store;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.products = products;
        this.categories = categories;
    }

    public String getStore() {
        return store;
    }

    public String getLocation() {
        return location;
    }

    @PropertyName("open_time")
    public Long getOpenTime() {
        return openTime;
    }

    @PropertyName("close_time")
    public Long getCloseTime() {
        return closeTime;
    }

    public Map<String, Map<String, Object>> getProducts() {
        return products;
    }

    @PropertyName("category")
    public Map<String, Map<String, String>> getCategories() {
        return categories;
    }
}
