package com.example.khajasangram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.khajasangram.Classes.UserdetailsClass;
import com.example.khajasangram.utilPackage.location.LocationUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements LocationUtil.LocationListener{
    Button signout, oatscafe, upf;
    private FirebaseAuth mAuth;
    private LocationUtil mLocationUtil;
    private DatabaseReference reference;

    //LocationUtil latlngreturner;
    LatLng value;

    AppCompatTextView locname;
     SharedPreferences editpreference;
     SharedPreferences editpreferencesignup;

    SharedPreferences.Editor editor;
    UserdetailsClass userdetailsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signout = findViewById(R.id.signout_home);
        oatscafe = findViewById(R.id.restrnt1);
        upf = findViewById(R.id.user_profile);

        editpreferencesignup = getSharedPreferences("UserDataSignup",0);

        editpreference = getSharedPreferences("UserDataHome",0);
        editor = editpreference.edit();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        upf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, UserprofileActivity.class));
            }
        });

        oatscafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(HomeActivity.this,RestaurantlistActivity.class));
            }
        });

        locname = (AppCompatTextView) findViewById(R.id.tv_location_picker);

        //latlngreturner=new LocationUtil(this);
        //value=latlngreturner.returnlatlng();

        //Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();

        mLocationUtil = new LocationUtil(HomeActivity.this);
        mLocationUtil.fetchApproximateLocation(this);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              mAuth.signOut();
              finish();
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

        String lat_string = String.valueOf(lat);
        String lng_String = String.valueOf(lng);

        editor.putString("latitude", lat_string);
        editor.putString("longitude", lng_String);
        editor.commit();

        String uid = mAuth.getCurrentUser().getUid();

        Toast.makeText(this, "uid= "+uid, Toast.LENGTH_SHORT).show();
        userdetailsClass = new UserdetailsClass(uid,editpreferencesignup.getString("firstname",null),editpreferencesignup.getString("lastname",null),editpreferencesignup.getString("email",null),lat_string, lng_String);
        reference.child(uid).setValue(userdetailsClass);
    }
}
