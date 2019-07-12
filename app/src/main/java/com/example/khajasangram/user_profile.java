package com.example.khajasangram;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class user_profile extends AppCompatActivity {
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();

                Intent intent = new Intent(user_profile.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickOpenSettings(View view) {
        Intent intent = new Intent(user_profile.this,EditPassword.class);
        startActivity(intent);
    }

    public void onClickOpenEditUser(View view) {
        Intent intent = new Intent(user_profile.this,EditProfile.class);
        startActivity(intent);
    }


}
