package com.move4mobile.apps.bite.objects;

import java.util.HashMap;

/**
 * Created by casvd on 28-11-2016.
 */

public class UserOrder {

    private HashMap<String, Integer> accessories;
    private int amount;

    public UserOrder() {

    }

    public UserOrder(HashMap<String, Integer> accessories, int amount) {
        this.accessories = accessories;
        this.amount = amount;
    }

    public HashMap<String, Integer> getAccessories() {
        return accessories;
    }

    public int getAmount() {
        return amount;
    }

    public void Add() {
        amount++;
    }

    public void Remove() {
        if(amount > 0) amount--;
    }
}
