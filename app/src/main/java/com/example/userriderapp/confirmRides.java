package com.example.userriderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class confirmRides extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextInputLayout til;
    TextView rideDets_tv;
    Button confirm_button;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    void init(){
        til = (TextInputLayout) findViewById(R.id.til_otp);
        rideDets_tv = (TextView) findViewById(R.id.textView_new_ride_info);
        confirm_button = (Button) findViewById(R.id.button_confirm_ride_driver);

        confirm_button.setClickable(false);
        confirm_button.setVisibility(View.INVISIBLE);
        til.setVisibility(View.INVISIBLE);


        database.getReference("rides").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                Ride r = snapshot.getValue(Ride.class);
                rideDets_tv.setText("New ride from "+r.source+" to "+r.dest+" with distance "+String.valueOf(r.dist)+" and price "+String.valueOf(r.cost)+". Enter OTP to confirm.");
                til.setVisibility(View.VISIBLE);

                confirm_button.setClickable(true);
                confirm_button.setVisibility(View.VISIBLE);
                confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int otp = Integer.valueOf(til.getEditText().getText().toString());

                        if(otp==r.otp){
                            DatabaseReference ref= database.getReference("confirmed").push();
                            database.getReference("rides/"+snapshot.getKey()).removeValue();




//
//
                            r.rid = ref.getKey();
                            ref.setValue(r);

                            ref.setValue(r);

                            database.getReference("users/"+r.uid+"/confirmed").push().setValue(ref.getKey());

                            database.getReference("users/"+mAuth.getUid()+"/confirmed").push().setValue(ref.getKey());


                            rideDets_tv.setText("Ride from "+r.source+" to "+r.dest+" with distance "+String.valueOf(r.dist)+" and price "+String.valueOf(r.cost)+" confirmed!");
                            confirm_button.setClickable(false);
                            confirm_button.setVisibility(View.INVISIBLE);

                            til.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Toast.makeText(confirmRides.this, "OTP not correct!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_rides);

        mAuth= FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference(mAuth.getUid()+"/rides");

        init();


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
                Intent i = new Intent(confirmRides.this, MainActivity.class);
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