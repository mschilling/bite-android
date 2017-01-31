package com.move4mobile.apps.bite.objects;

/**
 * Created by casvd on 31-1-2017.
 */

public class ArchiveProduct {

    private int amount;
    private String name;
    private long price;

    public ArchiveProduct() {

    }

    public ArchiveProduct(int amount, String name, long price) {

        this.amount = amount;
        this.name = name;
        this.price = price;
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
}
