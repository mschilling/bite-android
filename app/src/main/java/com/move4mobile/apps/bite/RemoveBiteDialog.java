package com.move4mobile.apps.bite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by casvd on 9-11-2016.
 */

public class RemoveBiteDialog extends AppCompatActivityFireAuth {

    private AlertDialog alertDialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefBite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        mDatabase = FirebaseDatabase.getInstance();
        mRefBite = mDatabase.getReference("orders").child(key);

        showRemoveBiteDialog();
    }

    private void showRemoveBiteDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Remove Bite")
                    .setMessage("Ur ye sure ye want tae remove th' Bite?")
                    .setPositiveButton("Aye", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRefBite.setValue(null);
                            finish();
                        }
                    })
                    .setNegativeButton("Na", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog != null) alertDialog.dismiss();
    }
}

