package com.example.help4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobWishlist extends AppCompatActivity {

    // Get Database Reference
    DatabaseReference reference;

    // Declare Recylcer View
    RecyclerView recyclerView;

    // Array List of Job Wishlist
    ArrayList<WishList> jobwishlist;

    // Set Adapter
    MyJobWishlist adapter;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Keys
    ArrayList<String> keys = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_wishlist);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        recyclerView = findViewById(R.id.MyJobWishListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobwishlist = new ArrayList<WishList>();

        reference = FirebaseDatabase.getInstance().getReference().child("jobwishlist").child(mFirebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    WishList wl = dataSnapshot1.getValue(WishList.class);
                    jobwishlist.add(wl);
                    keys.add(dataSnapshot1.getKey());
                }
                adapter = new MyJobWishlist(JobWishlist.this, jobwishlist, keys);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Show Error Message
            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        deleteItem(position);
                        Toast.makeText(JobWishlist.this,"Successfully Remove From Wishlist!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(JobWishlist.this, JobWishlist.class);
                        startActivity(intent);
                        finish();
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }

    public void deleteItem(int position) {
        String key = keys.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("jobwishlist").child(mFirebaseUser.getUid());
        ref.child(key).removeValue();
    }
}
