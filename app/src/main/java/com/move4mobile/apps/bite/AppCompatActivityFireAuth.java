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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by casvd on 3-11-2016.
 */

public abstract class AppCompatActivityFireAuth extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "ActivityFireAuth";

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private FirebaseDatabase database;
    //private DatabaseReference myConnectionsRef;
    private DatabaseReference lastOnlineRef;
    private DatabaseReference connectedRef;
    private ValueEventListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        mAuth.removeAuthStateListener(this);
        if(listener == null) return;
        connectedRef.removeEventListener(listener);
        listener = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
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

        if(listener != null) return;

        //myConnectionsRef = database.getReference("users").child(user.getUid()).child("connections");
        lastOnlineRef = database.getReference("users").child(user.getUid()).child("last_online");
        connectedRef = database.getReference(".info/connected");

        listener = connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {
                    onConnect();
                    //DatabaseReference con = myConnectionsRef.push();
                    //con.setValue(Boolean.TRUE);

                    // when this device disconnects, remove it
                    //con.onDisconnect().removeValue();

                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                } else {
                    onDisconnect();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Listener was cancelled at .info/connected");
            }
        });
    }

    protected void onDisconnect() {
        Log.d(TAG, "NOT CONNECTED");
    }

    protected void onConnect() {
        Log.d(TAG, "CONNECTED");
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
