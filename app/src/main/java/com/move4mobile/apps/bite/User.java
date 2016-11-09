package com.move4mobile.apps.bite;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by casvd on 9-11-2016.
 */

public class User {

    private String username;
    private String name;
    private String email;
    private String photo_url;

    public User(String username, String name, String email, String photo_url) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
    }
    public User(FirebaseUser user) {
        this.username = user.getDisplayName();
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.photo_url = String.valueOf(user.getPhotoUrl());
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
