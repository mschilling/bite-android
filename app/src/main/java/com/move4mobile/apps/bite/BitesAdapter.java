package com.move4mobile.apps.bite;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by casvd on 8-11-2016.
 */

public class BitesAdapter extends FirebaseRecyclerAdapter<Bite, BiteViewHolder> {

    private static final String TAG = "BitesAdapter";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefStoreName;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public BitesAdapter(Class<Bite> modelClass, int modelLayout, Class<BiteViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void populateViewHolder(final BiteViewHolder viewHolder, final Bite model, int position) {

        mRefStoreName = mDatabase.getReference("stores/"+model.getStore());
        mRefStoreName.child("data").child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e(TAG, dataSnapshot.toString());
                    viewHolder.mTextViewCustom.setText(dataSnapshot.getValue(String.class).toUpperCase());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        // TODO: Set the View
        Log.e(TAG, "Creating View... " + getRef(position).getKey());
    }

}
