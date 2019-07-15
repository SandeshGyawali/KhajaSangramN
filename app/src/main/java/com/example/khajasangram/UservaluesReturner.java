package com.example.khajasangram;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UservaluesReturner {
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String fname;

    public String getuser_id()
    {
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        return uid;
    }
    public String getuser_namefirst(String id)
    {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String uid = snapshot.child("id").getValue(String.class);
                    if(uid.equals(id))
                    {
                        fname = snapshot.child("fname").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return fname;

    }
}
