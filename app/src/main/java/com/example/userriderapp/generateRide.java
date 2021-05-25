package com.example.userriderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class generateRide extends AppCompatActivity {

    private static final String TAG = "generateRide";

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    ConstraintLayout cl_ride_dets, cl_ride_inputs;

    int otp,cost, dist;
    String source,dest;

    TextInputLayout tl1,tl2,tl3;
    Button confirm_button, cancel_button,book_button;
    TextView ride_dets_tv, ride_booked_tv;


    void init(){
        ride_dets_tv = (TextView) findViewById(R.id.textView_ride_dets);
        ride_booked_tv = (TextView) findViewById(R.id.textView_ride_booked_dets);
        cl_ride_dets = (ConstraintLayout) findViewById(R.id.constraintLayout_ride_dets);
        cl_ride_inputs = (ConstraintLayout) findViewById(R.id.cl_ride_inputs);

        confirm_button = (Button) findViewById(R.id.button_confirm);
        book_button = (Button) findViewById(R.id.button_book);
        cancel_button = (Button) findViewById(R.id.button_cancel);

        tl1 = (TextInputLayout) findViewById(R.id.textInputLayout_source);
        tl2 = (TextInputLayout) findViewById(R.id.textInputLayout_dest);
        tl3 = (TextInputLayout) findViewById(R.id.textInputLayout_dist);

        cl_ride_dets.setVisibility(View.INVISIBLE);


        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                source = tl1.getEditText().getText().toString();
                dest=tl2.getEditText().getText().toString();

                String string_dist = tl3.getEditText().getText().toString();

                if(source.equals("") || dest.equals("") || string_dist.equals("")){
                    Toast.makeText(generateRide.this, "Please enter Source, Destination and Distance!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Random rnd = new Random();

                    otp= 1000 + rnd.nextInt(9000);
                    cl_ride_inputs.setAlpha((float)(0.1));
                    book_button.setClickable(false);
                    tl1.getEditText().setFocusable(false);
                    tl2.getEditText().setFocusable(false);
                    tl3.getEditText().setFocusable(false);

                    cl_ride_dets.setVisibility(View.VISIBLE);


                    dist = Integer.valueOf(string_dist);
                    cost = dist * 5;
                    ride_dets_tv.setText("Confirm ride from " + source + " to " + dest + " with a distance of " + String.valueOf(dist) + "km costing " + Integer.valueOf(cost) + "$?");
                }


            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: confirm button");

                DatabaseReference myRef = database.getReference("rides");
                myRef = myRef.push();


                Ride r = new Ride();
                r.cost = cost;
                r.dest = dest;
                r.dist = dist;
                r.source = source;
                r.review=-1;
                r.otp = otp;
                r.uid = mAuth.getUid();
                r.rid = myRef.getKey();


                myRef.setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(generateRide.this, "Booked successfully!", Toast.LENGTH_SHORT).show();

                            ride_booked_tv.setText("Ride from "+source+" to "+dest+" with a distance of "+String.valueOf(dist)+"km costing "+Integer.valueOf(cost)+"$ confirmed!\nPlease provide otp: "+String.valueOf(otp)+" to driver on boarding the cab.");
                        }
                        else{
                            Toast.makeText(generateRide.this, "Ride not booked! Please try again!", Toast.LENGTH_SHORT).show();
                        }
                        cl_ride_dets.setVisibility(View.INVISIBLE);
                        cl_ride_inputs.setAlpha(1);
                        tl1.getEditText().setFocusable(true);
                        tl2.getEditText().setFocusable(true);
                        tl3.getEditText().setFocusable(true);
                        book_button.setClickable(true);
                        book_button.setText("Book new ride");
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ride);

        mAuth = FirebaseAuth.getInstance();

        init();

//        TextInputLayout itl;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.history:
                startActivity(new Intent(this, riderHistory.class));
                break;
            case R.id.proifle:
                startActivity(new Intent(this, showProfile.class));
                break;
            case R.id.signout:
                mAuth.signOut();
                Intent i = new Intent(generateRide.this, MainActivity.class);
// set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}