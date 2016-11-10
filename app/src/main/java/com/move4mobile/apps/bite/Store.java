package com.move4mobile.apps.bite;

/**
 * Created by casvd on 10-11-2016.
 */

public class Store {

    private String name;
    private String location;

    public Store() {

    }

    public Store(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
