package com.example.userriderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class riderHistory extends AppCompatActivity {

    private static final String TAG = "riderHistory";
    RecyclerView recyclerView;
    adapter madapter;

    ArrayList<Ride> rides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_history);


        Log.d(TAG, "onCreate: here");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("users/"+mAuth.getUid()+"/confirmed");

        recyclerView=(RecyclerView) findViewById(R.id.historyList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        madapter=new adapter(rides,this);
        recyclerView.setAdapter(madapter);
        madapter.notifyDataSetChanged();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

//                Log.d(TAG, "onChildAdded: ");
//                Toast.makeText(riderHistory.this, "In child added!", Toast.LENGTH_SHORT).show();

                String rid = (String) snapshot.getValue();

                Log.d(TAG, "onChildAdded: "+rid);

                DatabaseReference rideref = database.getReference("confirmed/"+rid);
                rideref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Ride r = dataSnapshot.getValue(Ride.class);
                        rides.add(r);
//                Log.d(TAG, "onChildAdded: "+String.valueOf(rides.size()));
                        madapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

//                Ride r = snapshot.getValue(Ride.class);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}