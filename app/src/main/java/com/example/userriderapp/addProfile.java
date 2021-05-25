package com.example.userriderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addProfile extends AppCompatActivity {

    Button save_profile_btn;
    TextInputLayout til1, til2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    void init(){
        save_profile_btn = (Button) findViewById(R.id.button_save_profile);
        til1 = (TextInputLayout) findViewById(R.id.til_name);
        til2 = (TextInputLayout) findViewById(R.id.til_phone);


        save_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = til1.getEditText().getText().toString();
                String phone = til2.getEditText().getText().toString();

                if(name.equals("") || phone.equals("")){
                    Toast.makeText(addProfile.this, "Please provide Name and Phone!", Toast.LENGTH_SHORT).show();
                }
                else{
                    profile p  = new profile();
                    p.name=name;p.phone=phone;
                    p.email = mAuth.getCurrentUser().getEmail();
                    p.isRider = MainActivity.isRider;
                    database.getReference("users/"+mAuth.getUid()+"/profile/").push().setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(addProfile.this, "Profile saved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(addProfile.this, generateRide.class);
// set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(addProfile.this, "Could not save profile info. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        init();
    }
}