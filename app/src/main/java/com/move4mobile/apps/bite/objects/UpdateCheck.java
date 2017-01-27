package com.move4mobile.apps.bite.objects;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.PropertyName;
import com.move4mobile.apps.bite.BuildConfig;

/**
 * Created by casvd on 27-1-2017.
 */

public class UpdateCheck {

    @PropertyName("version_code")
    private int code;
    @PropertyName("version_name")
    private String name;

    public UpdateCheck () {

    }

    public UpdateCheck(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @PropertyName("version_name")
    public String getName() {
        return name;
    }

    @PropertyName("version_code")
    public int getCode() {
        return code;
    }

    public boolean isUpdate() {
        boolean update = false;
        if(code > BuildConfig.VERSION_CODE){
            switch (name.compareToIgnoreCase(BuildConfig.VERSION_NAME)) {
                case -1:
                    //less
                    //Log.e("NAME", "-1");
                    break;
                case 0:
                    //equal
                    //Log.e("NAME", "0");
                    break;
                case 1:
                    //bigger
                    //Log.e("NAME", "1");
                default:
                    //Way bigger
                    //Log.e("NAME", "default");
                    update = true;
                    break;
            }
        }
        return update;
    }
}
