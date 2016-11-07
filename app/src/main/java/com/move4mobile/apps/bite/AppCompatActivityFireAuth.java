package com.move4mobile.apps.bite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by casvd on 3-11-2016.
 */

public abstract class AppCompatActivityFireAuth extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "ActivityFireAuth";

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            onLoggedIn();
        } else {
            // User is signed out
            onLoggedOut();
        }
    }

    protected void onLoggedIn() {
        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
    }

    protected void onLoggedOut() {
        Log.d(TAG, "onAuthStateChanged:signed_out");
    }

    protected boolean isLoggedIn() {
        return user != null;
    }

    protected FirebaseUser getUser() {
        return user;
    }

    protected void signOut() {
        Log.d(TAG, "FirebaseAuth: Signing out");
        mAuth.signOut();
    }

    protected void signIn(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            signInWithCredentialException(task.getException());
                        }
                        // ...
                    }
                });
    }

    protected void signInWithCredentialException(Exception exception) {
        Log.d(TAG, "signInWithCredential", exception);
    }
}
