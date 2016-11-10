package com.move4mobile.apps.bite;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by casvd on 9-11-2016.
 */

public class User {

    private String display_name;
    private String name;
    private String email;
    private String photo_url;

    public User(String display_name, String name, String email, String photo_url) {
        this.display_name = display_name;
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
    }

    public User(FirebaseUser user) {
        this.display_name = user.getDisplayName();
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.photo_url = String.valueOf(user.getPhotoUrl());
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photo_url;
    }
}
