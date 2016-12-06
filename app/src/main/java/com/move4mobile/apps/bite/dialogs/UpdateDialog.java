package com.move4mobile.apps.bite.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.move4mobile.apps.bite.BiteApplication;
import com.move4mobile.apps.bite.BuildConfig;

public class UpdateDialog extends Activity {

    private static boolean active = false;
    private static UpdateDialog _instance = null;
    private AlertDialog alertDialog;

    public static UpdateDialog getInstance() {
        if(_instance == null){
            _instance = new UpdateDialog();
        }
        return _instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        _instance = this;
        showUpdateMessage();
    }

    public void showUpdateMessage() {
        if(alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Bite Update");
            alertDialog.setMessage("Oi fella der is an update fo ye!\r\n" +
                    "\r\nYer version: " + BuildConfig.VERSION_NAME +
                    "\r\nNew version: " + BiteApplication.getNewVersionName());
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Roger dat fam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    active = false;
                }
            });
            alertDialog.show();
        } else {
            alertDialog.setMessage("Oi fella der is an update fo ye!\r\n" +
                    "\r\nYer version: " + BuildConfig.VERSION_NAME +
                    "\r\nNew version: " + BiteApplication.getNewVersionName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) alertDialog.dismiss();
    }

    public static boolean isActive() {
        return active;
    }
}
