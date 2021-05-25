package com.example.userriderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class showProfile extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();


    TextView name, phone, email, isRider;

    void init(){
        name = (TextView) findViewById(R.id.textView_name_profile);
        phone = (TextView) findViewById(R.id.textView_phone_profile);
        email = (TextView) findViewById(R.id.textView_email_profile);
        isRider = (TextView) findViewById(R.id.textView_isRider);
    }

    private static final String TAG = "showProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        init();

        DatabaseReference myRef = database.getReference("users/"+mAuth.getUid()+"/profile");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

//                Log.d(TAG, "onChildAdded: ");
//                Toast.makeText(riderHistory.this, "In child added!", Toast.LENGTH_SHORT).show();

                profile p =  snapshot.getValue(profile.class);
                name.setText(p.name);
                phone.setText(p.phone);
                isRider.setText((p.isRider)?"Rider":"Driver");
                email.setText(p.email);

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