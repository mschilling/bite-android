package com.move4mobile.apps.bite;

/**
 * Created by casvd on 18-11-2016.
 */

public class MenuItem {

    private String accessory_group;
    private String category;
    private String name;
    private long price;

    public MenuItem() {

    }

    public MenuItem(String name, long price) {
        this.name = name;
        this.price = price;

    }

    public MenuItem(String accessory_group, String category, String name, long price) {
        this.accessory_group = accessory_group;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public String getAccessory_group() {
        return accessory_group;
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
}
