package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserprofileActivity extends AppCompatActivity {
    TextView fname,lname;
    Button button;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        fname = findViewById(R.id.first_name);
        lname = findViewById(R.id.last_name);
        button = findViewById(R.id.submit);

        UservaluesReturner uservaluesReturner = new UservaluesReturner();

        String id = uservaluesReturner.getuser_id();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String user_id = snapshot.child("id").getValue(String.class);
                    if(id.equals(user_id))
                    {
                        fname.setText(snapshot.child("fname").getValue(String.class));
                        lname.setText(snapshot.child("lname").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
