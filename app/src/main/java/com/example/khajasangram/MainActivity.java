package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email_txt,pw_txt;
    Button login_btn;
    TextView signup_label;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_txt = findViewById(R.id.email_login);
        pw_txt = findViewById(R.id.password_login);

        login_btn = findViewById(R.id.login_btn);

        signup_label = findViewById(R.id.signup_label);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null || mAuth.getCurrentUser() != null) {
            signup_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, SignupActivity.class);
                    startActivity(i);
                }
            });

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signInWithEmailAndPassword(email_txt.getText().toString(), pw_txt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    startActivity(new Intent(MainActivity.this, HomepageActivity.class));
                                } else if (!(mAuth.getCurrentUser().isEmailVerified())) {
                                    Toast.makeText(MainActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
       // else
       // {
       //     startActivity(new Intent(MainActivity.this,HomeActivity.class));
       // }
    }
}
