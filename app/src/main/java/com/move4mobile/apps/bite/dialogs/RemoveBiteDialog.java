package com.move4mobile.apps.bite.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by casvd on 9-11-2016.
 */

public class RemoveBiteDialog extends Activity {

    private AlertDialog alertDialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefBite;
    private DatabaseReference mRefBiteOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        mDatabase = FirebaseDatabase.getInstance();
        mRefBite = mDatabase.getReference("orders").child(key);
        mRefBiteOrder = mDatabase.getReference("user_order").child(key);

        showRemoveBiteDialog();
    }

    private void showRemoveBiteDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setTitle("Remove Bite");
            alertDialog.setMessage("Ur ye sure ye want tae remove th' Bite?");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aye", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRefBite.updateChildren(new HashMap<String, Object>(){
                                {
                                    put("action", "remove");
                                }
                            });
                            finish();
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Na", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog != null) alertDialog.dismiss();
    }
}

