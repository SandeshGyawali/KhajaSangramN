package com.example.khajasangram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.khajasangram.Classes.UserdetailsClass;
import com.example.khajasangram.Fragments.Dashboard_fragment;
//import com.example.khajasangram.Fragments.Home_fragment;
import com.example.khajasangram.Fragments.Notification_fragment;
import com.example.khajasangram.Fragments.Search_fragment;
import com.example.khajasangram.Fragments.Try_fragment;
import com.example.khajasangram.SQLite.Databasehelper;
import com.example.khajasangram.utilPackage.location.LocationUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomepageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LocationUtil.LocationListener {

    SharedPreferences preferences, preferences_location;
    SharedPreferences.Editor editor;
    LatLng value;
    private LocationUtil mLocationUtil;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String lat_string, lng_string;

    Boolean loc_checker= false;
    SharedPreferences uidpreference;
    SharedPreferences.Editor editor1;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);


        mLocationUtil = new LocationUtil(HomepageActivity.this);
        mLocationUtil.fetchApproximateLocation(this);

        preferences = getSharedPreferences("UserDataSignup",0);
        mAuth = FirebaseAuth.getInstance();

        uidpreference = getSharedPreferences("uidpreference",0);
        getLastLocation();

        if(loc_checker != false) {
            //loadFragment(new Home_fragment());
            loadFragment(new Try_fragment());
        }
    }

    private boolean loadFragment(Fragment fragment)
    {
        if (fragment != null) {

            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.fragment_container,fragment).
                    commit();

            return true;
        }

            return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch(menuItem.getItemId())
        {
            case R.id.navigation_home:

                //fragment = new Home_fragment();
                fragment = new Try_fragment();
                 break;
        case R.id.navigation_dashboard:

                fragment = new Dashboard_fragment();
                break;
        case R.id.navigation_notifications:

                fragment = new Notification_fragment();
                break;

            case R.id.navigation_search:
                fragment = new Search_fragment();
                break;
        }


        return loadFragment(fragment);
    }
    private void getLastLocation() {
        mLocationUtil.fetchApproximateLocation(this);
    }

    @Override
    public void onLocationReceived(@NonNull LatLng location, @NonNull String addressString) {
        value = location;
       // Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
        //locname.setText(addressString);
        Double lat,lng;

        lat = location.latitude;
        lng = location.longitude;

        lat_string = String.valueOf(lat);
        lng_string = String.valueOf(lng);

        //Toast.makeText(this, "lat= "+lat_string+" lon= "+lng_string, Toast.LENGTH_SHORT).show();

        String uid = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        editor1 = uidpreference.edit();
        editor1.putString("uid",uid);
        editor1.commit();

        Toast.makeText(this, "uid= "+uid, Toast.LENGTH_SHORT).show();
        //userdetailsClass = new UserdetailsClass(uid,preferences.getString("firstname",null),preferences.getString("lastname",null),preferences.getString("email",null),lat_string, lng_string);
        //reference.child(uid).setValue(userdetailsClass);
        reference.child("latitude").setValue(lat_string);
        reference.child("longitude").setValue(lng_string);
        loc_checker = true;

        //loadFragment(new Home_fragment());
        loadFragment(new Try_fragment());

        /*editor.putString("latitude", lat_string);
        editor.putString("longitude", lng_String);
        editor.commit();*/

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            loadFragment(new Try_fragment());
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public void dialogbuilder(Context c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Want to exit?");
        builder.setCancelable(true);
        builder.setMessage("Your account will be logged out.");

        builder.setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

    }
}

