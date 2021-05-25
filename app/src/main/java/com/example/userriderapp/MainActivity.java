package com.example.userriderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    boolean isRegister=false;

    Switch switch1;
    TextInputLayout til1, til2;
    Button login_register_button;
    TextView login_register_tv, rider_tv, driver_tv,register_tv;
    ConstraintLayout cl;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    void init(){
        switch1 = (Switch) findViewById(R.id.switch1);
        til1 = (TextInputLayout) findViewById(R.id.textInputLayout_email);
        til2 = (TextInputLayout) findViewById(R.id.textInputLayout_password);
        login_register_button = (Button) findViewById(R.id.login_register_button);
        login_register_tv = (TextView) findViewById(R.id.login_register_tv);
        cl = (ConstraintLayout) findViewById(R.id.constraintLayout_switch);
        rider_tv = (TextView) findViewById(R.id.rider_tv);
        driver_tv = (TextView) findViewById(R.id.driver_tv);
        register_tv = (TextView) findViewById(R.id.register_tv);

        driver_tv.setVisibility(View.INVISIBLE);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rider_tv.setVisibility(View.INVISIBLE);
                    driver_tv.setVisibility(View.VISIBLE);
                    register_tv.setVisibility(View.INVISIBLE);
                    isRider=false;
                    login_register_button.setText("Login as Driver");
                    login_register_tv.setText("Login as Driver");
                }
                else{
                    isRider=true;
                    login_register_button.setText("Login as Rider");
                    login_register_tv.setText("Login as Rider");
                    rider_tv.setVisibility(View.VISIBLE);
                    driver_tv.setVisibility(View.INVISIBLE);
                    register_tv.setVisibility(View.VISIBLE);
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isRegister) {
                    isRegister=true;
                    cl.setVisibility(View.INVISIBLE);
                    login_register_button.setText("Register");
                    login_register_tv.setText("Register as Rider");
                    register_tv.setText("Click here to login");
                }
                else{
                    isRegister=false;
                    cl.setVisibility(View.VISIBLE);
                    login_register_button.setText("Login");
                    if(isRider) {
                        login_register_tv.setText("Login as Rider");
                    }
                    else{
                        login_register_tv.setText("Login as Driver");
                    }
                    register_tv.setText("Click here to Register");
                }
            }
        });

        login_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(login_register_button.getApplicationWindowToken(),0);
                String email = til1.getEditText().getText().toString(), password=til2.getEditText().getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(MainActivity.this, "Email or Password not filled!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isRegister) {
                        register(email, password);
                    } else {
                        login(email, password);
                    }
                }
            }
        });


    }

    FirebaseAuth mAuth;
    static boolean isRider = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

//        database.getReference("users/sy49qD80DhSfaOlMNSsbaWR3Jrx1").removeValue();

        database.getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: "+snapshot.getKey());
                Log.d(TAG, "onChildAdded: "+snapshot.getValue().toString());
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

        database.getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: "+snapshot.getKey());
                Log.d(TAG, "onChildAdded: "+snapshot.getValue().toString());
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



//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(user!=null)mAuth.signOut();
//
//        AuthCredential credential = EmailAuthProvider
//                .getCredential("driver@gmail.com", "driver");
//
//        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
//                    user1.delete()
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "User account deleted.");
//                                    }
//                                }
//                            });
//                }
//            }
//        });


        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.


        // Prompt the user to re-provide their sign-in credentials
//        user1.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//
//                    }
//                });

// ...
// Initialize Firebase Auth

//        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            DatabaseReference myRef = database.getReference("users/"+mAuth.getUid()+"/rider");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    if(value.equals("true")) {
                        Intent i = new Intent(MainActivity.this, generateRide.class);
// set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, confirmRides.class);
// set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        else{

            setContentView(R.layout.activity_main);

            init();
        }

    }

    void login(String email,String pass){

        Log.d(TAG, "login: "+email+" and "+pass);

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference myRef = database.getReference("users/"+mAuth.getUid()+"/rider");
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    String value = dataSnapshot.getValue(String.class);

                                    Log.d(TAG, "onDataChange: value: "+value);

                                    if(value.equals("true") && isRider) {
                                        Intent i = new Intent(MainActivity.this, generateRide.class);
// set the new task and clear flags
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                    else if(value.equals("false") && !isRider) {
                                        Intent i = new Intent(MainActivity.this, confirmRides.class);
// set the new task and clear flags
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }else {
                                        mAuth.signOut();
                                        Toast.makeText(MainActivity.this, "You are not registered as "+((isRider)?"Rider":"Driver")+". Please try with correct setting!", Toast.LENGTH_SHORT).show();
                                    }

                                    Log.d(TAG, "Value is: " + value);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });

//                            if(isRider && )

                            // Sign in success, update UI with the signed-in user's information

//                            startActivity(new Intent(MainActivity.this, generateRide.class));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            DatabaseReference myRef = database.getReference("users/"+mAuth.getUid()+"/rider");
                            myRef.setValue(String.valueOf("true"));

                            Intent i = new Intent(MainActivity.this, addProfile.class);
// set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}