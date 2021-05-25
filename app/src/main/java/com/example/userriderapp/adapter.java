package com.example.userriderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userriderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Ride> rides=new ArrayList<>();

    private static final String TAG = "adapter";

    Activity activity;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();


    public adapter(ArrayList<Ride> rides,Activity activity){
        this.rides=rides;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemeView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        RecyclerView.ViewHolder holder=new Holder(itemeView);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Holder classHolder = (Holder) holder;
        try{
            classHolder.src_dest_tv.setText(rides.get(position).source + " to "+ rides.get(position).dest);
            classHolder.cost_tv.setText(String.valueOf(rides.get(position).cost)+"$");
            classHolder.dist_tv.setText(String.valueOf(rides.get(position).dist)+"km");
            if(rides.get(position).review==-1)
                classHolder.review_tv.setText("Rating: Not rated");
            else
                classHolder.review_tv.setText("Rating: "+String.valueOf(rides.get(position).review));


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView src_dest_tv,dist_tv,cost_tv, review_tv;
        Ride r;


        public Holder(View itemView) {

            super(itemView);
//            this.r=r;
            src_dest_tv = (TextView) itemView.findViewById(R.id.textview_source_dest_history);
            dist_tv = (TextView) itemView.findViewById(R.id.textView_dist_history);
            cost_tv = (TextView) itemView.findViewById(R.id.textView_cost_history);
            review_tv = (TextView) itemView.findViewById(R.id.textView_review);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.d(TAG, "onDataChange: Reached here -1");
            r = rides.get(getLayoutPosition());
            Log.d(TAG, "onDataChange: Reached here 0");
            DatabaseReference myRef = database.getReference("users/"+mAuth.getUid()+"/rider");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "onDataChange: Reached here 1");
                    Context context = itemView.getContext();
                    if(value.equals("true")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.rating, null);
                        dialogBuilder.setView(dialogView);

                        Log.d(TAG, "onDataChange: Reached here 2");

                        Button button = (Button)dialogView.findViewById(R.id.button_submit_rating);
                        AlertDialog alertDialog = dialogBuilder.create();
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SeekBar sb = (SeekBar) dialogView.findViewById(R.id.seekBar);
                                int rating = sb.getProgress();

                                HashMap<String,Object> m = new HashMap<String, Object>();
                                m.put("review",rating);


                                database.getReference("confirmed/"+r.rid).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            rides.remove(r);
                                            r.review = rating;

                                            notifyDataSetChanged();
                                        }
                                        else{

                                        }
                                    }

                                });
                                alertDialog.cancel();


                                //Commond here......

                            }
                        });


                        alertDialog.show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        }
    }
}