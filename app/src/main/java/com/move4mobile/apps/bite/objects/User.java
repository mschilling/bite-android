package com.move4mobile.apps.bite.objects;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ServerValue;

/**
 * Created by casvd on 9-11-2016.
 */

public class User {

    @PropertyName("display_name")
    private String displayName;

    private String name;
    private String email;

    @PropertyName("photo_url")
    private String photoUrl;

    @PropertyName("last_online")
    private long lastOnline;

    private boolean admin;

    public User() {

    }

    public User(String displayName, String name, String email, String photoUrl, boolean admin) {
        this.displayName = displayName;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.admin = admin;
    }

    public User(String displayName, String name, String email, String photoUrl, long lastOnline, boolean admin) {
        this.displayName = displayName;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.lastOnline = lastOnline;
        this.admin = admin;
    }

    public User(FirebaseUser user) {
        this.displayName = user.getDisplayName();
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.photoUrl = String.valueOf(user.getPhotoUrl());
        this.lastOnline = System.currentTimeMillis();
    }

    @PropertyName("display_name")
    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @PropertyName("photo_url")
    public String getPhotoUrl() {
        return photoUrl;
    }

    @PropertyName("last_online")
    public long getLastOnline() {
        return lastOnline;
    }

    public boolean isAdmin() {
        return admin;
    }
}
