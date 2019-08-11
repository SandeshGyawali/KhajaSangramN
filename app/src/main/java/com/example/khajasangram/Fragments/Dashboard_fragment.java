package com.example.khajasangram.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.khajasangram.R;
import com.example.khajasangram.SQLite.Databasehelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard_fragment extends Fragment {
    ImageView img;
    LinearLayout update, about, settings;
    TextView txt;
    EditText fname, lname, email;
    Button update_btn;

    FirebaseAuth auth;
    DatabaseReference reference;
    FrameLayout first_layout, second_layout;
    String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_fragment,null);

        first_layout = view.findViewById(R.id.first_layout);
        second_layout = view.findViewById(R.id.second_layout);

        first_layout.setVisibility(View.VISIBLE);
        second_layout.setVisibility(View.INVISIBLE);

        img = view.findViewById(R.id.user_image);
        txt = view.findViewById(R.id.username);
        update = view.findViewById(R.id.update);
        about = view.findViewById(R.id.about);
        settings = view.findViewById(R.id.settings);

        fname = view.findViewById(R.id.new_fname);
        lname = view.findViewById(R.id.new_lname);
        email = view.findViewById(R.id.new_email);

        update_btn = view.findViewById(R.id.update_btn);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        display_profile();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                first_layout.setVisibility(View.INVISIBLE);
                second_layout.setVisibility(View.VISIBLE);


                update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fname_txt = fname.getText().toString();
                        String lname_txt = lname.getText().toString();
                        String email_txt = email.getText().toString();

                        DatabaseReference reference;
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                        reference.child("fname").setValue(fname_txt);
                        reference.child("lname").setValue(lname_txt);
                        reference.child("email").setValue(email_txt);

                        Toast.makeText(getContext(), "Profile successfully Updated!!!", Toast.LENGTH_SHORT).show();

                        second_layout.setVisibility(View.INVISIBLE);
                        first_layout.setVisibility(View.VISIBLE);
                        display_profile();
                    }
                });

            }
        });



        return view;
    }

    private void display_profile() {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uname = dataSnapshot.child("fname").getValue(String.class);
                Toast.makeText(getContext(), "name= "+uname, Toast.LENGTH_SHORT).show();

                txt.setText(uname);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

