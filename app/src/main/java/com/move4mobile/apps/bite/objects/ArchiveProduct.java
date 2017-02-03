package com.move4mobile.apps.bite.objects;

import com.google.firebase.database.PropertyName;

/**
 * Created by casvd on 31-1-2017.
 */

public class ArchiveProduct {

    private int amount;
    private String name;
    private long price;
    @PropertyName("isSauce")
    private boolean sauce;

    public ArchiveProduct() {

    }

    public ArchiveProduct(int amount, String name, long price, boolean sauce) {
        this.amount = amount;
        this.name = name;
        this.price = price;
        this.sauce = sauce;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    @PropertyName("isSauce")
    public boolean isSauce() {
        return sauce;
    }
}
