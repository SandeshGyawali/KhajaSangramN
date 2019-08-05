package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khajasangram.Classes.UserdetailsClass;
import com.example.khajasangram.utilPackage.location.LocationUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements LocationUtil.LocationListener{
    EditText fname,lname,email,pw,cfm_pw;
    Button submit;
    private FirebaseAuth mAuth, mAuth2;
    private DatabaseReference reference;
    String mail_id_user,fname_txt,lname_txt,latitude_txt,longitude_txt;
    SharedPreferences preferences, preferences_location;
    SharedPreferences.Editor editor,editor_location;
    private LocationUtil mLocationUtil;
    private Boolean location_fetch_checker = false;
    String lat_string,lng_string;

    //LocationUtil latlngreturner;
    LatLng value;
    AppCompatTextView locname;

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
        preferences_location = getSharedPreferences("UserDataSignupLocation",0);

        submit = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();
        mAuth2 = FirebaseAuth.getInstance();

        locname = (AppCompatTextView) findViewById(R.id.tv_location_picker);


        mLocationUtil = new LocationUtil(SignupActivity.this);
        mLocationUtil.fetchApproximateLocation(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fname_txt = fname.getText().toString();
                lname_txt = lname.getText().toString();
                mail_id_user = email.getText().toString();

                editor = preferences.edit();
                editor_location = preferences_location.edit();


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
                                        if (location_fetch_checker == false) {
                                            Toast.makeText(SignupActivity.this, "Please wait for the loaction to get fetched.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (task.isSuccessful()) {
                                                DatabaseReference reference;
                                                Toast.makeText(SignupActivity.this, "Registered Succesfully.Please check your email", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                String uid = mAuth2.getCurrentUser().getUid();

                                                reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

                                                UserdetailsClass userdetailsClass;
                                                userdetailsClass = new UserdetailsClass(uid,fname_txt,lname_txt,mail_id_user,lat_string, lng_string);

                                                reference.setValue(userdetailsClass);

                                            } else {
                                                Toast.makeText(SignupActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
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
    public void onLocationClick(View view) {
        getLastLocation();

    }

    private void getLastLocation() {
        mLocationUtil.fetchApproximateLocation(this);
    }

    @Override
    public void onLocationReceived(@NonNull LatLng location, @NonNull String addressString) {
        value = location;
        Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
        locname.setText(addressString);
        Double lat,lng;

        lat = location.latitude;
        lng = location.longitude;

        lat_string = String.valueOf(lat);
        lng_string = String.valueOf(lng);


        editor_location = preferences_location.edit();
        editor_location.putString("latitude", lat_string);
        editor_location.putString("longitude", lng_string);
        editor_location.commit();

        location_fetch_checker = true;

    }
}
