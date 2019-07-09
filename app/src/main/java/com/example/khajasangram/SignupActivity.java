package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText fname,lname,email,pw,cfm_pw;
    Button submit;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    String mail_id_user,fname_txt,lname_txt,latitude_txt,longitude_txt;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname = findViewById(R.id.fname_signup);
        lname = findViewById(R.id.lname_signup);
        email = findViewById(R.id.email_signup);
        pw = findViewById(R.id.password_signup);
        cfm_pw = findViewById(R.id.cnfmpassword_signup);

        preferences = getSharedPreferences("UserDataSignup",0);

        submit = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fname_txt = fname.getText().toString();
                lname_txt = lname.getText().toString();
                mail_id_user = email.getText().toString();

                editor = preferences.edit();


                editor.putString("firstname", fname_txt);
                editor.putString("lastname", lname_txt);
                editor.putString("email", mail_id_user);
                editor.commit();

                if(!(pw.getText().toString().equals(cfm_pw.getText().toString())))
                {
                    Toast.makeText(SignupActivity.this, "Password and confirm password did not match.Please try again!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Registered Succesfully.Please check your email", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(SignupActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        }
}
