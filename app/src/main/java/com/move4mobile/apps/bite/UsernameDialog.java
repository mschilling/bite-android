package com.move4mobile.apps.bite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by casvd on 9-11-2016.
 */

public class UsernameDialog extends AppCompatActivityFireAuth {

    private AlertDialog alertDialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        mRefDisplayName = mDatabase.getReference("users");
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
        mRefDisplayName = mRefDisplayName.child(getUser().getUid()).child("data").child("display_name");
        showUsernameDialog();
    }

    private void showUsernameDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Waaat ter call yer");

            Resources r = getBaseContext().getResources();
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            int px1 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    20,
                    r.getDisplayMetrics()
            );
            params2.setMargins(px1, 0, px1, 0);

            final RelativeLayout relativeLayout = new RelativeLayout(this);
            final EditText input = new EditText(this);
            relativeLayout.addView(input);
            input.setLayoutParams(params2);
            input.setHint(getUser().getDisplayName().split(" ")[0]);
            input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            //input.setTypeface(BiteApplication.Fonts.COMPASSE);
            alertDialog.setView(relativeLayout);

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Call me loike dis", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String username = input.getText().toString();
                    if(username.isEmpty()) username = getUser().getDisplayName().split(" ")[0];

                    mRefDisplayName.setValue(username);

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

