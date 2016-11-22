package com.move4mobile.apps.bite;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by casvd on 3-11-2016.
 */

public class LoginActivity extends AppCompatActivityFireAuth implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount account;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUsers;

    private ImageView imageView;
    private TextView textView;
    private RelativeLayout showUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oauth_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) //Activity // OnConnectionFailedListener
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mDatabase = FirebaseDatabase.getInstance();
        mRefUsers = mDatabase.getReference("users");

        SignInButton mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        imageView = ((ImageView) findViewById(R.id.google_account_image));
        imageView.setOnClickListener(this);
        textView = ((TextView) findViewById(R.id.google_account_name));
        showUser = ((RelativeLayout) findViewById(R.id.google_user_details));
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();

        getSilentLogin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_account_image:
                firebaseAuthWithGoogle(account);
                break;
            case R.id.sign_in_button:
                signOut();
                signIn();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void signOut() {
        super.signOut();
        // Google sign out
        Log.d(TAG, "GoogleAuth: Signing out");
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //updateUI(null);
                    }
                });
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();

        checkIfUserExists();

        if(progressDialog != null){
            progressDialog.cancel();
            progressDialog = null;
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkIfUserExists() {
        Log.e(TAG,"checkUser");
        final DatabaseReference userRef = mRefUsers.child(getUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,dataSnapshot.toString());
                if(dataSnapshot.getValue() == null || dataSnapshot.getChildrenCount() <= 1){
                    Log.e(TAG, "null");
                    User user = new User(getUser());
                    userRef.setValue(user);
                    Log.d(TAG, "Added user");

                    changeUserName();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }

    private void changeUserName() {
        Intent intent = new Intent(this, UsernameDialog.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "onActivitiyResult: " + result.isSuccess() + " | " + result.getStatus());
            Toast.makeText(this, result.getStatus().toString(), Toast.LENGTH_SHORT).show();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        progressDialog = ProgressDialog.show(LoginActivity.this, "Firebase Auth", "Please wait...", true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideNavigationBar();
            }
        });

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        super.signIn(credential);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void signInWithCredentialException(Exception exception) {
        super.signInWithCredentialException(exception);
        if(progressDialog != null){
            progressDialog.cancel();
            progressDialog = null;
        }
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showSilentLogin() {
        if(imageView.getVisibility() == View.GONE){
            imageView.setVisibility(View.VISIBLE);
        }
        if(showUser.getVisibility() == View.INVISIBLE){
            showUser.setVisibility(View.VISIBLE);
        }
    }

    private void hideSilentLogin() {
        if(imageView.getVisibility() == View.VISIBLE){
            imageView.setVisibility(View.GONE);
        }
        if(showUser.getVisibility() == View.VISIBLE){
            showUser.setVisibility(View.INVISIBLE);
        }
    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void getSilentLogin() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone())
        {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done" and the GoogleSignInResult will be available instantly.
            Log.d("TAG", "Got cached sign-in");

            GoogleSignInResult result = opr.get();
            account = result.getSignInAccount();

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 148, metrics);

            Glide.with(this).load(account.getPhotoUrl() + "?sz=" + px).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });

            textView.setText(account.getDisplayName());

            showSilentLogin();
        } else {
            Log.d("TAG", "No Cached login found");
            hideSilentLogin();
        }
    }
}
