package com.move4mobile.apps.bite.objects;

import com.google.firebase.database.PropertyName;

/**
 * Created by casvd on 18-11-2016.
 */

public class MenuItem {

    @PropertyName("accessory_group")
    private String accessoryGroup;
    private String category;
    private String name;
    private long price;
    @PropertyName("isSauce")
    private boolean sauce;

    public MenuItem() {

    }

    public MenuItem(String name, long price) {
        this.name = name;
        this.price = price;

    }

    public MenuItem(String accessoryGroup, String category, String name, long price, boolean sauce) {
        this.accessoryGroup = accessoryGroup;
        this.category = category;
        this.name = name;
        this.price = price;
        this.sauce = sauce;
    }

    @PropertyName("accessory_group")
    public String getAccessoryGroup() {
        return accessoryGroup;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    @PropertyName("isSauce")
    public boolean isSauce() {
        return sauce;
    }
}
