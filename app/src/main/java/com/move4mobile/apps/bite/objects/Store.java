package com.move4mobile.apps.bite.objects;

import com.google.firebase.database.PropertyName;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by casvd on 10-11-2016.
 */

public class Store {

    private int id;
    private String name;
    private String location;

    @PropertyName("category")
    private Map<String, HashMap<String, String>> categories;

    public Store() {

    }

    public Store(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Store(int id, String name, String location, Map<String, HashMap<String, String>> categories) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @PropertyName("category")
    public Map<String, HashMap<String, String>> getCategories() {
        return categories;
    }

    public int getId() {
        return id;
    }
}
